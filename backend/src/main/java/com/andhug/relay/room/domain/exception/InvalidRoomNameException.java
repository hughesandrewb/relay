package com.andhug.relay.room.domain.exception;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

public class InvalidRoomNameException extends InvalidArgumentException {
  public InvalidRoomNameException() {
    super("Room name cannot be null or blank");
  }
}
