package com.andhug.relay.room.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andhug.relay.room.domain.model.RoomType;

import java.util.List;
import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

    List<RoomEntity> findByWorkspaceId(UUID workspaceId);
    List<RoomEntity> findByParticipants_ProfileIdAndType(UUID profileId, RoomType type);
}
