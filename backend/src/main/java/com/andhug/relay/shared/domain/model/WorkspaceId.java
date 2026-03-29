package com.andhug.relay.shared.domain.model;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.utils.IdValidator;
import java.util.UUID;

public record WorkspaceId(UUID value) {

  public WorkspaceId {
    if (value == null) {
      throw new InvalidArgumentException("WorkspaceId cannot be null");
    }
  }

  public static WorkspaceId generate() {
    return new WorkspaceId(UUID.randomUUID());
  }

  public static WorkspaceId of(UUID value) {
    return new WorkspaceId(value);
  }

  public static WorkspaceId of(String value) {
    return new WorkspaceId(IdValidator.validate(value, WorkspaceId.class.getName()));
  }

  public String toString() {
    return value.toString();
  }
}
