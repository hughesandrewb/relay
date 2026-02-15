package com.andhug.relay.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GatewayOpcode {
    DISPATCH(0),
    HEARTBEAT(1);

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
