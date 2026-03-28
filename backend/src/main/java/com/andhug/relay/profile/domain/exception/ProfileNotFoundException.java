package com.andhug.relay.profile.domain.exception;

import java.util.UUID;

import com.andhug.relay.shared.domain.exception.NotFoundException;

public class ProfileNotFoundException extends NotFoundException {
    public ProfileNotFoundException(UUID profileId) {
        super("Could not find profile with id: " + profileId);
    }
}
