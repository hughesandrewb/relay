package com.andhug.relay.room.infrastructure.web.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record RoomDto(UUID id, String name, UUID workspaceId, Integer type) {}
