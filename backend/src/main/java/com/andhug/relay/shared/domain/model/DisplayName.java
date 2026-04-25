package com.andhug.relay.shared.domain.model;

public record DisplayName(String value) {

  private static final int MAX_LENGTH = 50;

  public DisplayName {
    if (value == null || value.isBlank()) {
      value = "New User";
    }
    if (value.length() > MAX_LENGTH) {
      value = value.substring(0, MAX_LENGTH);
    }
  }

  public static DisplayName of(String value) {
    return new DisplayName(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
