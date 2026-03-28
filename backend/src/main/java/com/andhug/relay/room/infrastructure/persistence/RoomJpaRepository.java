package com.andhug.relay.room.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

    List<RoomEntity> findByWorkspaceId(UUID workspaceId);
}
