package com.andhug.relay.invite.api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetInviteResponse(
        String code,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {}
