package com.andhug.relay.workspace.infrastructure.web.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record WorkspaceDto(UUID id, String name, UUID ownerId) {}
