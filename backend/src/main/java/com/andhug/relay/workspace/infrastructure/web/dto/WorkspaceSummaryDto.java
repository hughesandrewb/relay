package com.andhug.relay.workspace.infrastructure.web.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record WorkspaceSummaryDto(UUID id, String name, Boolean owner) {}
