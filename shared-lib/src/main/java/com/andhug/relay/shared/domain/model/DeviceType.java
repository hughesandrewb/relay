package com.andhug.relay.shared.domain.model;

public enum DeviceType {
  WEB(0),
  DESKTOP(1),
  MOBILE(2);

  private final int value;

  DeviceType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static DeviceType fromValue(int value) {
    for (DeviceType type : DeviceType.values()) {
      if (type.value == value) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid DeviceType value: " + value);
  }
}
