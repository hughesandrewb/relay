package com.andhug.relay.shared.domain.model;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.utils.IdValidator;
import java.util.UUID;

public record ProfileId(UUID value) {

  public ProfileId {
    if (value == null) {
      throw new InvalidArgumentException("ProfileId cannot be null");
    }
  }

  public static ProfileId generate() {
    return new ProfileId(UUID.randomUUID());
  }

  public static ProfileId of(UUID value) {
    return new ProfileId(value);
  }

  public static ProfileId of(String value) {
    return new ProfileId(IdValidator.validate(value, ProfileId.class.getName()));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
