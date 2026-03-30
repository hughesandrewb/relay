package com.andhug.relay.profile.application.handler;

import com.andhug.relay.profile.application.command.CreateProfileCommand;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.domain.repository.ProfileRepository;
import com.andhug.relay.shared.application.handler.SyncCommandHandler;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProfileHandler implements SyncCommandHandler<CreateProfileCommand, ProfileId> {

  private final ProfileRepository profileRepository;

  @Transactional
  public ProfileId handle(CreateProfileCommand command) {

    log.info("Creating profile for user: {}", command.id());

    var profile = Profile.create(command.id(), command.username());

    profileRepository.save(profile);

    return profile.getId();
  }
}
