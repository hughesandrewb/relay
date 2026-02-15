package com.andhug.relay.room;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RoomDto(
        UUID id,
        String name,
        UUID workspaceId
) {}
