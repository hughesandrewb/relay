package com.andhug.relay.shared.domain.model;

import com.andhug.relay.shared.application.utils.IdValidator;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import java.util.UUID;

public record RoomId(UUID value) {

  public RoomId {
    if (value == null) {
      throw new InvalidArgumentException("RoomId cannot be null");
    }
  }

  public static RoomId generate() {
    return new RoomId(UUID.randomUUID());
  }

  public static RoomId of(UUID value) {
    return new RoomId(value);
  }

  public static RoomId of(String value) {
    return new RoomId(IdValidator.validate(value, RoomId.class.getName()));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
