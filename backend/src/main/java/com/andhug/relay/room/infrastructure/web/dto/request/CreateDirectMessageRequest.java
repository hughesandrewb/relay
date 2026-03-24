package com.andhug.relay.room.infrastructure.web.dto.request;

import java.util.UUID;

public record CreateDirectMessageRequest(
        UUID recipientId
) {}
