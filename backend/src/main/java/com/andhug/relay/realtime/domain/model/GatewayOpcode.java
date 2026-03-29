package com.andhug.relay.realtime.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gateway opcodes that can be used to identify the type of payload being sent or received over the
 * gateway connection.
 */
public enum GatewayOpcode {

  /** Sent by server to dispatch an event. */
  DISPATCH(0),

  /** Sent by client or server to keep connection alive. */
  HEARTBEAT(1),

  /** Sent by client during initialization to start new session. */
  IDENTIFY(2),

  /** Sent by client to update the client's presence. */
  PRESENCE_UPDATE(3),

  /** Sent by client to resume a previous session that was disconnected. */
  RESUME(6),

  /** Sent by server to notify client to reconnect and resume immediately. */
  RECONNECT(7),

  /** Sent by server to notify client that the session has been invalidated. */
  INVALID_SESSION(9),

  /** Sent by server immediately after connecting with `heartbear_interval` for client to use. */
  HELLO(10),

  /** Sent by server in response to heartbeat, acknowledging that its been received. */
  HEARTBEAT_ACK(11);

  private int opcode;

  GatewayOpcode(int opcode) {
    this.opcode = opcode;
  }

  @JsonCreator
  public static GatewayOpcode fromOpcode(int opcode) {
    for (GatewayOpcode gatewayOpcode : GatewayOpcode.values()) {
      if (gatewayOpcode.opcode == opcode) {
        return gatewayOpcode;
      }
    }

    throw new IllegalArgumentException("Invalid opcode: " + opcode);
  }
}
