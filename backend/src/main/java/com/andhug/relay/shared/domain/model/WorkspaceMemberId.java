package com.andhug.relay.shared.domain.model;

import com.andhug.relay.shared.application.utils.IdValidator;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import java.util.UUID;

public record WorkspaceMemberId(UUID value) {

  public WorkspaceMemberId {
    if (value == null) {
      throw new InvalidArgumentException("WorkspaceMemberId cannot be null");
    }
  }

  public static WorkspaceMemberId generate() {
    return new WorkspaceMemberId(UUID.randomUUID());
  }

  public static WorkspaceMemberId of(UUID value) {
    return new WorkspaceMemberId(value);
  }

  public static WorkspaceMemberId of(String value) {
    return new WorkspaceMemberId(IdValidator.validate(value, WorkspaceMemberId.class.getName()));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
