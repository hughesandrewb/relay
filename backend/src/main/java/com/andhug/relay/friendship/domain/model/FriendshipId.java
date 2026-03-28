package com.andhug.relay.friendship.domain.model;

import java.util.UUID;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

public record FriendshipId(UUID value) {
    
    public FriendshipId {
        if (value == null) {
            throw new InvalidArgumentException("FriendshipId cannot be null");
        }
    }

    public static FriendshipId of(UUID value) {
        return new FriendshipId(value);
    }
}
