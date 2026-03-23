package com.andhug.relay.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * gateway opcodes that can be used to identify the type of payload being sent
 * or received over the gateway connection
 */
public enum GatewayOpcode {

    /**
     * sent by server to dispatch an event
     */
    DISPATCH(0),

    /**
     * sent by client or server to keep connection alive
     */
    HEARTBEAT(1),

    /**
     * sent by client during initialization to start new session
     */
    IDENTIFY(2),

    /**
     * sent by client to update the client's presence
     */
    PRESENCE_UPDATE(3),

    /**
     * sent by client to resume a previous session that was disconnected
     */
    RESUME(6),

    /**
     * sent by server to notify client to reconnect and resume immediately
     */
    RECONNECT(7),

    /**
     * sent by server to notify client that the session has been invalidated
     */
    INVALID_SESSION(9),

    /**
     * sent by server immediately after connecting with `heartbear_interval` for
     * client to use
     */
    HELLO(10),

    /**
     * sent by server in response to heartbeat, acknowledging that its been received
     */
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
