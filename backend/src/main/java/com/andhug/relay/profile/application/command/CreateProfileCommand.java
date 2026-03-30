package com.andhug.relay.profile.application.command;

import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.Builder;

@Builder
public record CreateProfileCommand(ProfileId id, String username)
    implements SyncCommand<ProfileId> {

  public CreateProfileCommand {
    if (id == null) {
      throw new InvalidArgumentException("id cannot be null");
    }
    if (username == null || username.isBlank()) {
      throw new InvalidArgumentException("username cannot be null or blank");
    }
  }

  public static CreateProfileCommand of(ProfileId id, String username) {
    return new CreateProfileCommand(id, username);
  }
}
