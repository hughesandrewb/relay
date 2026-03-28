package com.andhug.relay.shared.domain.model;

import java.util.UUID;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.utils.IdValidator;

public record RoomId(UUID value) {

    public RoomId {
        if (value == null) {
            throw new InvalidArgumentException("RoomId cannot be null");
        }
    }

    public static RoomId generate() {
        return new RoomId(UUID.randomUUID());
    }

    public static RoomId of(UUID value) {
        return new RoomId(value);
    }

    public static RoomId of(String value) {
        return new RoomId(IdValidator.validate(value, RoomId.class.getName()));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
