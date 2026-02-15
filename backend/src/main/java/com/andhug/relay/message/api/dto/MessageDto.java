package com.andhug.relay.message.api.dto;

import java.util.UUID;

public record MessageDto(
        long id,
        UUID authorId,
        UUID roomId,
        String content
) {}
