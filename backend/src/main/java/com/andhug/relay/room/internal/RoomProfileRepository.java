package com.andhug.relay.room.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomProfileRepository extends JpaRepository<RoomProfileEntity, UUID> {
}
