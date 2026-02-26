package com.andhug.relay.friend.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FriendSummaryDto(
        UUID id,
        String username,
        String accentColor
) {}
