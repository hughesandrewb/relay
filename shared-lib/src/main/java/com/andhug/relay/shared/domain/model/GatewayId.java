package com.andhug.relay.shared.domain.model;

import java.util.UUID;

public record GatewayId(UUID value) {

  public GatewayId {
    if (value == null) {
      throw new IllegalArgumentException("GatewayId cannot be null");
    }
  }

  public GatewayId(String value) {
    this(UUID.fromString(value));
  }

  public static GatewayId of(String value) {
    // TODO: replace this with the IdValidator
    return new GatewayId(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
