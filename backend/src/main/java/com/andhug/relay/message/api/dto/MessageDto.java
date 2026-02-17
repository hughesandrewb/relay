package com.andhug.relay.message.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MessageDto(
        long id,
        UUID authorId,
        UUID roomId,
        String content
) {}
