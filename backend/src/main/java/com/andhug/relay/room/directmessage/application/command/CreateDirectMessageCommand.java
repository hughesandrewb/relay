package com.andhug.relay.room.directmessage.application.command;

import com.andhug.relay.room.directmessage.domain.exceptions.NotEnoughParticipantsException;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Set;

public record CreateDirectMessageCommand(Set<ProfileId> participants) {

  public CreateDirectMessageCommand {
    if (participants == null) {
      throw new InvalidArgumentException("participants cannot be null");
    }
    if (participants.size() < 2) {
      throw new NotEnoughParticipantsException();
    }
  }

  public static CreateDirectMessageCommand of(ProfileId... participants) {
    return new CreateDirectMessageCommand(Set.of(participants));
  }
}
