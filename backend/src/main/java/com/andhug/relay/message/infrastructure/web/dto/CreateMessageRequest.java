package com.andhug.relay.message.infrastructure.web.dto;

import java.util.UUID;

public record CreateMessageRequest(
        UUID authorId,
        UUID roomId,
        String content
) {}
