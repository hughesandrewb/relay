package com.andhug.relay.profile.domain.exception;

import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.model.ProfileId;

public class ProfileNotFoundException extends NotFoundException {
  public ProfileNotFoundException(ProfileId profileId) {
    super("Could not find profile with id: " + profileId.toString());
  }
}
