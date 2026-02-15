package com.andhug.relay.room;

import com.andhug.relay.room.internal.RoomEntity;
import com.andhug.relay.room.internal.RoomMapper;
import com.andhug.relay.room.internal.RoomRepository;
import com.andhug.relay.workspace.api.events.WorkspaceCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public List<Room> getRoomsByProfileId(UUID profileId) {

        return roomRepository.findByParticipants_ProfileId(profileId)
                .stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByWorkspaceId(UUID workspaceId) {

        return roomRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    @Async
    @EventListener
    void on(WorkspaceCreatedEvent event) {

        var roomEntity = RoomEntity.builder()
                .name("general")
                .workspaceId(event.workspaceId())
                .build();

        roomRepository.save(roomEntity);

        log.info("Room created: {}", roomEntity);
    }
}
