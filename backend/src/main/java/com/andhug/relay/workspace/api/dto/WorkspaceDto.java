package com.andhug.relay.workspace.api.dto;

import java.util.UUID;

public record WorkspaceDto(
        UUID id,
        String name,
        UUID ownerId
) {}
