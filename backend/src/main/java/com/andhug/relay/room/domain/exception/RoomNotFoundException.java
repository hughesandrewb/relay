package com.andhug.relay.room.domain.exception;

import java.util.UUID;

import com.andhug.relay.shared.domain.exception.NotFoundException;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(UUID roomId) {
        super("Room not found with ID: " + roomId);
    }
}
