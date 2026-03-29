package com.andhug.relay.room.domain.exception;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

public class InvalidRoomParticipantException extends InvalidArgumentException {
  public InvalidRoomParticipantException() {
    super("Invalid room participant");
  }
}
