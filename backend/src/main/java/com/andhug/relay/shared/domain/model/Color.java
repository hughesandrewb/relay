package com.andhug.relay.shared.domain.model;

import com.andhug.relay.utils.RandomUtils;

public record Color(String value) {
  public Color {
    if (value == null) {
      value = RandomUtils.generateRandomColor();
    }
    if (!value.matches("^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
      value = RandomUtils.generateRandomColor();
    }
  }

  public static Color of(String value) {
    return new Color(value);
  }

  public static Color of(Integer value) {
    if (value == null) {
      return new Color(null);
    }
    String hex = String.format("%06X", (0xFFFFFF & value));
    return new Color(hex);
  }
}
