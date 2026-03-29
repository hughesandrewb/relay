package com.andhug.relay.room.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomProfileJpaRepository
    extends JpaRepository<RoomProfileEntity, RoomProfileKey> {}
