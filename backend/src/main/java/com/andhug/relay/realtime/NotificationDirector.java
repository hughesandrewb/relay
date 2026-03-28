package com.andhug.relay.realtime;

import com.andhug.relay.message.domain.events.MessageCreatedEvent;
import com.andhug.relay.message.infrastructure.web.dto.MessageDto;
import com.andhug.relay.profile.application.mapper.ProfileMapper;
import com.andhug.relay.profile.application.service.ProfileService;
import com.andhug.relay.realtime.dto.RealtimeMessagePayload;
import com.andhug.relay.realtime.registry.Connection;
import com.andhug.relay.realtime.registry.ConnectionRegistry;
import com.andhug.relay.room.application.mapper.RoomMapper;
import com.andhug.relay.room.domain.event.RoomUpdatedEvent;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.service.RoomDomainService;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.application.mapper.WorkspaceMapper;
import com.andhug.relay.workspace.domain.event.JoinedWorkspaceEvent;
import com.andhug.relay.workspace.domain.event.WorkspaceUpdatedEvent;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;
import com.andhug.relay.workspace.domain.service.WorkspaceDomainService;
import com.andhug.relay.workspacemembership.application.service.WorkspaceMemberQueryService;

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

    private final RoomDomainService roomService;

    private final WorkspaceDomainService workspaceService;

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceMemberQueryService workspaceMemberQueryService;

    private final ProfileService profileService;

    private final WorkspaceMapper workspaceMapper;

    private final RoomMapper roomMapper;

    private final ProfileMapper profileMapper;

    private final ConnectionRegistry connectionRegistry;

    private final Map<UUID, Set<UUID>> roomToProfiles = new ConcurrentHashMap<>(); // roomId -> profileId[]

    @ApplicationModuleListener
    void onMessageCreated(MessageCreatedEvent event) {

        log.info("Message created event received for message {}", event.getMessageId());

        Set<UUID> toNotify = roomToProfiles.get(event.getRoomId());

        var data = MessageDto.builder()
            .id(event.getMessageId().value())
            .author(profileMapper.toDto(profileService.getProfile(ProfileId.of(event.getAuthorId()))))
            .roomId(event.getRoomId())
            .content(event.getContent())
            .build();

        var message = RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.MESSAGE_CREATE)
            .data(data)
            .build();

        broadcast(toNotify, message);
    }

    @ApplicationModuleListener
    void onWorkspaceUpdate(WorkspaceUpdatedEvent event) {

        // this will get all the members of the workspace, even if they are offline
        // rethink this after implementing presence better
        Set<UUID> toNotify = workspaceService.getMemberIds(event.workspaceId().value())
            .stream()
            .collect(Collectors.toSet());

        Workspace workspace = workspaceRepository.findById(event.workspaceId());

        var message = RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.WORKSPACE_UPDATE)
            .data(workspaceMapper.toDto(workspace))
            .build();

        broadcast(toNotify, message);
    }

    @ApplicationModuleListener
    void onRoomUpdate(RoomUpdatedEvent event) {

        Set<UUID> toNotify = roomToProfiles.get(event.roomId().value());

        // TODO: get from room application service
        Room room = null; // roomService.getRoomById(event.roomId());

        var message = RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.ROOM_UPDATE)
            .data(roomMapper.toDto(room))
            .build();

        broadcast(toNotify, message);
    }

    private void broadcast(Set<UUID> recipients, RealtimeMessagePayload<Object> message) {
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

        Set<WorkspaceId> workspaces = workspaceMemberQueryService.getWorkspaceIdsOfProfile(ProfileId.of(profileId));

        for (WorkspaceId workspaceId : workspaces) {
            List<Room> rooms = roomService.getRoomsByWorkspaceId(workspaceId.value());

            log.info("Subscribing to {} rooms for workspace {}", rooms.size(), workspaceId);

            for (Room room : rooms) {
                roomToProfiles
                    .computeIfAbsent(room.getId().value(), k -> new HashSet<>())
                    .add(profileId);
            }
        }

        // List<Room> directMessages =  roomService.getDirectMessages(profileId);
        // for (Room room : directMessages) {

        //     log.info("Subscribing to direct message {}", room.getId());

        //     roomToProfiles
        //         .computeIfAbsent(room.getId().value(), k -> new HashSet<>())
        //         .add(profileId);
        // }
    }
}
