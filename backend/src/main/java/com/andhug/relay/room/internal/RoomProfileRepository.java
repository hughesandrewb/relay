package com.andhug.relay.room.internal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomProfileRepository extends JpaRepository<RoomProfileEntity, RoomProfileKey> {
}
