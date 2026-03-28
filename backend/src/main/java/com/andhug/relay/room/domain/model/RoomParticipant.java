package com.andhug.relay.room.domain.model;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;

public record RoomParticipant(ProfileId profileId) {
    
    public RoomParticipant {
        if (profileId == null) {
            throw new InvalidArgumentException("ProfileId cannot be null");
        }
    }
}
