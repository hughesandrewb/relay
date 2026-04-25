package com.andhug.relay.presence.domain.model;

import com.andhug.relay.shared.application.utils.IdValidator;
import java.util.UUID;

public record SessionId(UUID value) {

  public SessionId {
    if (value == null) {
      throw new IllegalArgumentException("SessionId cannot be null");
    }
  }

  public static SessionId of(UUID value) {
    return new SessionId(value);
  }

  public static SessionId of(String value) {
    return new SessionId(IdValidator.validate(value, SessionId.class.getName()));
  }

  public static SessionId generate() {
    return new SessionId(UUID.randomUUID());
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
