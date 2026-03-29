package com.andhug.relay.profile.application.service;

import com.andhug.relay.profile.application.command.CreateProfileCommand;
import com.andhug.relay.profile.domain.model.DisplayName;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.domain.repository.ProfileRepository;
import com.andhug.relay.shared.domain.model.Color;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileCommandService {

  private final ProfileRepository profileRepository;

  @Transactional
  public Profile createProfile(CreateProfileCommand command) {
    log.info("Creating profile for user: {}", command.userId());

    var profile =
        Profile.builder()
            .id(command.userId())
            .username(command.username())
            .displayName(command.displayName().orElse(DisplayName.of(command.username())))
            .accentColor(command.accentColor().orElse(Color.of((String) null)))
            .build();

    return profileRepository.save(profile);
  }
}
