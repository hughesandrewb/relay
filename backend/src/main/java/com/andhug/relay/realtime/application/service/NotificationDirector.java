package com.andhug.relay.realtime.application.service;

import com.andhug.relay.message.application.service.MessageQueryService;
import com.andhug.relay.message.domain.events.MessageCreatedEvent;
import com.andhug.relay.realtime.application.domain.event.RealtimeConnectionOpenedEvent;
import com.andhug.relay.realtime.application.domain.model.Connection;
import com.andhug.relay.realtime.application.domain.model.GatewayEvent;
import com.andhug.relay.realtime.application.domain.model.GatewayOpcode;
import com.andhug.relay.realtime.infrastructure.web.dto.RealtimeMessagePayload;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDirector {

  private final RoomDomainService roomService;

  private final WorkspaceDomainService workspaceService;

  private final WorkspaceRepository workspaceRepository;

  private final WorkspaceMemberQueryService workspaceMemberQueryService;

  private final MessageQueryService messageQueryService;

  private final WorkspaceMapper workspaceMapper;

  private final RoomMapper roomMapper;

  private final ConnectionRegistry connectionRegistry;

  private final Map<UUID, Set<UUID>> roomToProfiles =
      new ConcurrentHashMap<>(); // roomId -> profileId[]

  void onMessageCreated(MessageCreatedEvent event) {

    log.info("Message created event received for message {}", event.getMessageId());

    // Set<UUID> toNotify = roomToProfiles.get(event.getRoomId().value());

    // MessageDto data = messageQueryService.getMessage(event.getMessageId());

    // var message =
    //     RealtimeMessagePayload.builder()
    //         .opcode(GatewayOpcode.DISPATCH)
    //         .type(GatewayEvent.MESSAGE_CREATE)
    //         .data(data)
    //         .build();

    // broadcast(toNotify, message);
  }

  @ApplicationModuleListener
  void onWorkspaceUpdate(WorkspaceUpdatedEvent event) {

    // this will get all the members of the workspace, even if they are offline
    // rethink this after implementing presence better
    Set<UUID> toNotify =
        workspaceService.getMemberIds(event.workspaceId().value()).stream()
            .collect(Collectors.toSet());

    Workspace workspace = workspaceRepository.findById(event.workspaceId());

    var message =
        RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.WORKSPACE_UPDATE)
            .data(workspaceMapper.toDto(workspace))
            .build();

    broadcast(toNotify, message);
  }

  @ApplicationModuleListener
  void onRoomUpdate(RoomUpdatedEvent event) {

    Set<UUID> toNotify = roomToProfiles.get(event.getRoomId().value());

    // TODO: get from room application service
    Room room = null; // roomService.getRoomById(event.roomId());

    var message =
        RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(GatewayEvent.ROOM_UPDATE)
            .data(roomMapper.toDto(room))
            .build();

    broadcast(toNotify, message);
  }

  public void dispatch(List<ProfileId> recipients, GatewayEvent event, Object data) {
    var message =
        RealtimeMessagePayload.builder()
            .opcode(GatewayOpcode.DISPATCH)
            .type(event)
            .data(data)
            .build();

    Set<UUID> recipientIds = recipients.stream().map(ProfileId::value).collect(Collectors.toSet());

    broadcast(recipientIds, message);
  }

  private void broadcast(Set<UUID> recipients, RealtimeMessagePayload<Object> message) {
    for (UUID profileId : recipients) {
      Connection connection = connectionRegistry.getConnection(ProfileId.of(profileId));

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

  @ApplicationModuleListener
  void onRealtimeConnectionOpened(RealtimeConnectionOpenedEvent event) {
    log.info("Profile {} connected, subscribing to rooms", event.getProfileId());
    this.subscribeToRooms(event.getProfileId().value());
  }

  public void subscribeToRooms(UUID profileId) {

    Set<WorkspaceId> workspaces =
        workspaceMemberQueryService.getWorkspaceIdsOfProfile(ProfileId.of(profileId));

    for (WorkspaceId workspaceId : workspaces) {
      List<Room> rooms = roomService.getRoomsByWorkspaceId(workspaceId.value());

      log.info("Subscribing to {} rooms for workspace {}", rooms.size(), workspaceId);

      for (Room room : rooms) {
        roomToProfiles.computeIfAbsent(room.getId().value(), k -> new HashSet<>()).add(profileId);
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
