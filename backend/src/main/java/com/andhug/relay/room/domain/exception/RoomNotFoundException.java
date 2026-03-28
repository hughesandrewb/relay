package com.andhug.relay.room.domain.exception;

import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.model.RoomId;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(RoomId roomId) {
        super("Room not found with ID: " + roomId.value());
    }
}
