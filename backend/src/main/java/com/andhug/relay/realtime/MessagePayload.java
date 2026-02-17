package com.andhug.relay.realtime;

import lombok.Builder;

@Builder
public record MessagePayload(
        GatewayOpcode op,
        Object data
) {}
