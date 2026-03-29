package com.andhug.relay.room.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

  List<RoomEntity> findByWorkspaceId(UUID workspaceId);
}
