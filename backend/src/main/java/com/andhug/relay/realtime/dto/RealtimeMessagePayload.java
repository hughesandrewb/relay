package com.andhug.relay.realtime.dto;

import com.andhug.relay.realtime.GatewayEvent;
import com.andhug.relay.realtime.GatewayOpcode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RealtimeMessagePayload<T>(

        @JsonProperty("op")
        GatewayOpcode opcode,

        @JsonProperty("d")
        T data,

        @JsonProperty("s")
        Integer sequence,

        @JsonProperty("t")
        GatewayEvent type
) {}
