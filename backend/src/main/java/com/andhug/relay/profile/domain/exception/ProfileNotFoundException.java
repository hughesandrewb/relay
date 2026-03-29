package com.andhug.relay.profile.domain.exception;

import com.andhug.relay.shared.domain.exception.NotFoundException;
import java.util.UUID;

public class ProfileNotFoundException extends NotFoundException {
  public ProfileNotFoundException(UUID profileId) {
    super("Could not find profile with id: " + profileId);
  }
}
