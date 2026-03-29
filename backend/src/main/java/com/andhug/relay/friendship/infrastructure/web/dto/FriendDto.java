package com.andhug.relay.friendship.infrastructure.web.dto;

import java.util.UUID;

public record FriendDto(UUID id, String username, String displayName, String accentColor) {
  public FriendDto(UUID id, String username, String displayName, Integer accentColor) {
    this(id, username, displayName, String.format("%06X", (0xFFFFFF & accentColor)));
  }
}
