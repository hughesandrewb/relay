package com.andhug.relay.profile.exception;

import java.util.UUID;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(UUID profileId) {

        super("Could not find profile with id: " + profileId);
    }
}
