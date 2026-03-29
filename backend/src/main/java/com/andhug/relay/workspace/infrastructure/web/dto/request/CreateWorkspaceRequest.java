package com.andhug.relay.workspace.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateWorkspaceRequest(@NotBlank String name) {}
