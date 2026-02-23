package com.andhug.relay.realtime;

import com.andhug.relay.message.api.events.MessageCreated;
import com.andhug.relay.realtime.registry.Connection;
import com.andhug.relay.realtime.registry.ConnectionRegistry;
import com.andhug.relay.room.Room;
import com.andhug.relay.room.RoomService;
import com.andhug.relay.workspace.api.Workspace;
import com.andhug.relay.workspace.api.WorkspaceService;
import com.andhug.relay.workspace.api.events.JoinedWorkspaceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDirector {

    private final RoomService roomService;

    private final WorkspaceService workspaceService;

    private final ConnectionRegistry connectionRegistry;

    private final Map<UUID, Set<UUID>> roomToProfiles = new ConcurrentHashMap<>(); // roomId -> profileId[]

    @ApplicationModuleListener
    void onMessageCreated(MessageCreated event) {

        Set<UUID> toNotify = roomToProfiles.get(event.roomId());

        var message = MessagePayload.builder()
                .op(GatewayOpcode.DISPATCH)
                .data(event.message())
                .build();

        for (UUID profileId : toNotify) {
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

            log.info("Subscribing to {} rooms for {}", rooms.size(), workspace.getId());

            for (Room room : rooms) {
                roomToProfiles
                        .computeIfAbsent(room.getId(), k -> new HashSet<>())
                        .add(profileId);
            }
        }
    }
}
