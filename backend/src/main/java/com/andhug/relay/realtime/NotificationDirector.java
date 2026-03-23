package com.andhug.relay.realtime;

import com.andhug.relay.message.api.events.MessageCreated;
import com.andhug.relay.realtime.dto.RealtimeMessagePayload;
import com.andhug.relay.realtime.registry.Connection;
import com.andhug.relay.realtime.registry.ConnectionRegistry;
import com.andhug.relay.room.api.Room;
import com.andhug.relay.room.api.RoomService;
import com.andhug.relay.room.api.events.UpdateRoom;
import com.andhug.relay.room.internal.RoomMapper;
import com.andhug.relay.workspace.api.Workspace;
import com.andhug.relay.workspace.api.WorkspaceService;
import com.andhug.relay.workspace.api.events.JoinedWorkspaceEvent;
import com.andhug.relay.workspace.api.events.WorkspaceUpdated;
import com.andhug.relay.workspace.internal.WorkspaceMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDirector {

    private final RoomService roomService;

    private final WorkspaceService workspaceService;

    private final WorkspaceMapper workspaceMapper;

    private final RoomMapper roomMapper;

    private final ConnectionRegistry connectionRegistry;

    private final Map<UUID, Set<UUID>> roomToProfiles = new ConcurrentHashMap<>(); // roomId -> profileId[]

    @ApplicationModuleListener
    void onMessageCreated(MessageCreated event) {

        Set<UUID> toNotify = roomToProfiles.get(event.roomId());

        var message = RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.MESSAGE_CREATE)
            .data(event.message())
            .build();

        broadcast(toNotify, message);}

    @ApplicationModuleListener
    void onWorkspaceUpdate(WorkspaceUpdated event) {

        // this will get all the members of the workspace, even if they are offline
        // rethink this after implementing presence better
        Set<UUID> toNotify = workspaceService.getMemberIds(event.workspaceId())
            .stream()
            .collect(Collectors.toSet());

        var message = RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.WORKSPACE_UPDATE)
            .data(workspaceMapper.toDto(event.workspace()))
            .build();

        broadcast(toNotify, message);
    }

    @ApplicationModuleListener
    void onRoomUpdate(UpdateRoom event) {

        Set<UUID> toNotify = roomToProfiles.get(event.roomId());

        var message = RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.ROOM_UPDATE)
            .data(roomMapper.toDto(event.room()))
            .build();

        broadcast(toNotify, message);
    }

    private void broadcast(Set<UUID> recipients, RealtimeMessagePayload message) {
        for (UUID profileId : recipients) {
            Connection connection = connectionRegistry.getConnection(profileId);

            if (connection == null || !connection.isActive()) {
                log.warn("Connection {} is not open", profileId);
                continue;
            }

            connection.sendMessage(message);
        }
    }

    @ApplicationModuleListener
    void onJoinedWorkspace(JoinedWorkspaceEvent event) {
        subscribeToRooms(event.profileId());
    }

    public void subscribeToRooms(UUID profileId) {

        List<Workspace> workspaces = workspaceService.findAllByProfileId(profileId);

        for (Workspace workspace : workspaces) {
            List<Room> rooms = roomService.getRoomsByWorkspaceId(workspace.getId());

            log.info("Subscribing to {} rooms for workspace {}", rooms.size(), workspace.getId());

            for (Room room : rooms) {
                roomToProfiles
                    .computeIfAbsent(room.getId(), k -> new HashSet<>())
                    .add(profileId);
            }
        }

        List<Room> directMessages =  roomService.getDirectMessages(profileId);
        for (Room room : directMessages) {

            log.info("Subscribing to direct message {}", room.getId());

            roomToProfiles
                .computeIfAbsent(room.getId(), k -> new HashSet<>())
                .add(profileId);
        }
    }
}
