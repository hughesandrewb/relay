package com.andhug.relay.realtime;

import com.andhug.relay.message.api.events.MessageCreated;
import com.andhug.relay.realtime.registry.Connection;
import com.andhug.relay.realtime.registry.ConnectionRegistry;
import com.andhug.relay.room.Room;
import com.andhug.relay.room.RoomService;
import com.andhug.relay.workspace.api.Workspace;
import com.andhug.relay.workspace.api.WorkspaceService;
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

    private final Map<UUID, Set<UUID>> roomToProfiles = new ConcurrentHashMap<>();  // roomId -> profileId[]

    @ApplicationModuleListener
    void on(MessageCreated event) {

        Set<UUID> toNotify = roomToProfiles.get(event.roomId());

        for (UUID profileId : toNotify) {
            Connection connection = connectionRegistry.getConnection(profileId);

            if (connection == null || !connection.isActive()) {
                log.warn("Connection {} is not open", profileId);
                continue;
            }

            connection.sendMessage(event.content());  // TODO: send actual message op
        }
    }

    public void subscribeToRooms(UUID profileId) {

        List<Workspace> workspaces = workspaceService.findAllByProfileId(profileId);

        for (Workspace workspace : workspaces) {
            List<Room> rooms = roomService.getRoomsByWorkspaceId(workspace.getId());

            log.info("Subscribing to {} rooms for {}", rooms.size(), workspace.getId());

            for (Room room : rooms) {
                Set<UUID> roomIds = roomToProfiles.get(room.getId());
                if (roomIds == null) {
                    roomIds = new HashSet<>();
                }
                roomIds.add(profileId);
                roomToProfiles.put(room.getId(), roomIds);
            }
        }
    }
}
