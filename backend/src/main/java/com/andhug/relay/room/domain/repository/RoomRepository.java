package com.andhug.relay.room.domain.repository;

import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.shared.domain.model.RoomId;

public interface RoomRepository {
    Room findById(RoomId roomId);
    void save(Room room);
}
