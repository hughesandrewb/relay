package com.andhug.relay.room.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

    List<RoomEntity> findByWorkspaceId(UUID workspaceId);
    List<RoomEntity> findByParticipants_ProfileId(UUID profileId);
}
