package com.andhug.relay.realtime.dto;

import lombok.Builder;

@Builder
public record RealtimeTicketDto(
        String code
) {}
