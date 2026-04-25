package com.andhug.relay.gateway.domain.model;

public enum Opcode {
  DISPATCH(0),
  HEARTBEAT(1),
  PRESENCE_UPDATE(3),
  RESUME(6),
  RECONNECT(7),
  INVALID_SESSION(9),
  HELLO(10),
  HEARTBEAT_ACK(11);

  private final int code;

  Opcode(int code) {
    this.code = code;
  }

  public static Opcode fromCode(int code) {
    for (Opcode opcode : Opcode.values()) {
      if (opcode.code == code) {
        return opcode;
      }
    }
    throw new IllegalArgumentException("Unknown opcode code: " + code);
  }

  public int getCode() {
    return code;
  }
}
