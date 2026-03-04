package com.andhug.relay.room.api.dto.request;

import java.util.UUID;

public record CreateDirectMessageRequest(
        UUID recipientId
) {}
