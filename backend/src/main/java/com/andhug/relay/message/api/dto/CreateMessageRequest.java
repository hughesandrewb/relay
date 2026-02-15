package com.andhug.relay.message.api.dto;

import java.util.UUID;

public record CreateMessageRequest(
        UUID authorId,
        UUID roomId,
        String content
) {}
