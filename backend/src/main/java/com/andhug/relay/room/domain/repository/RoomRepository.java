package com.andhug.relay.room.domain.repository;

import java.util.UUID;

import com.andhug.relay.room.domain.model.Room;

public interface RoomRepository {
    Room findById(UUID roomId);
    Room save(Room room);
}
