package com.andhug.relay.realtime.domain.repository;

import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.ProfilePresence;

public interface PresenceRepository {
  public void saveProfilePresence(ProfileId profileId, ProfilePresence profilePresence);

  public ProfilePresence findByProfileId(ProfileId profileId);
}
