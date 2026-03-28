package com.andhug.relay.room.infrastructure.web.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RoomDto(
        UUID id,
        String name,
        UUID workspaceId,
        Integer type
) {}
