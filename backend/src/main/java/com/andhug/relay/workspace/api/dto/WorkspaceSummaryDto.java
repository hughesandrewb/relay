package com.andhug.relay.workspace.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkspaceSummaryDto(
        UUID id,
        String name,
        Boolean owner
) {}
