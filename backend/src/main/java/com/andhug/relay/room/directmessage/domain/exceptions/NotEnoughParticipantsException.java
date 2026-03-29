package com.andhug.relay.room.directmessage.domain.exceptions;

import com.andhug.relay.shared.domain.exception.DomainException;

public class NotEnoughParticipantsException extends DomainException {

  public NotEnoughParticipantsException() {
    super("DirectMessage must have at least 2 participants");
  }
}
