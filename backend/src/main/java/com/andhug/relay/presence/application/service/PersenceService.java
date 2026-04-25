package com.andhug.relay.presence.application.service;

import com.andhug.relay.realtime.domain.repository.PresenceRepository;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.ProfilePresence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersenceService {

  private final PresenceRepository presenceRepository;

  public ProfilePresence getProfilePresence(ProfileId profileId) {
    return presenceRepository.findByProfileId(profileId);
  }
}
