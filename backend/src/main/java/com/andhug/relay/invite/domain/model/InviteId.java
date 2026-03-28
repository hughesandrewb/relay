package com.andhug.relay.invite.domain.model;

import java.util.UUID;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.utils.IdValidator;

public record InviteId(UUID value) {
    
    public InviteId {
        if (value == null) {
            throw new InvalidArgumentException("InviteId cannot be null");
        }
    }

    public static InviteId generate() {
        return new InviteId(UUID.randomUUID());
    }

    public static InviteId of(UUID value) {
        return new InviteId(value);
    }

    public static InviteId of(String value) {
        return new InviteId(IdValidator.validate(value, InviteId.class.getName()));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
