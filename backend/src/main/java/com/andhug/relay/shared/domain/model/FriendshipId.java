package com.andhug.relay.shared.domain.model;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import java.util.UUID;

public record FriendshipId(UUID value) {

  public FriendshipId {
    if (value == null) {
      throw new InvalidArgumentException("FriendshipId cannot be null");
    }
  }

  public static FriendshipId of(UUID value) {
    return new FriendshipId(value);
  }

  public static FriendshipId generate() {
    return new FriendshipId(UUID.randomUUID());
  }
}
