package com.andhug.relay.gateway.domain.model;

public record SessionId(String value) {

  public SessionId {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Session ID cannot be null or blank");
    }
  }

  public static SessionId of(String value) {
    return new SessionId(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
