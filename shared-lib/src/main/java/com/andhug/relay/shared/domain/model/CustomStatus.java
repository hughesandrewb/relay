package com.andhug.relay.shared.domain.model;

public record CustomStatus(String value) {

  private static final int MAX_LENGTH = 140;

  public CustomStatus {
    if (value == null) {
      value = "";
    }
    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "CustomStatus cannot be longer than " + MAX_LENGTH + " characters");
    }
  }

  public static CustomStatus of(String value) {
    return new CustomStatus(value);
  }
}
