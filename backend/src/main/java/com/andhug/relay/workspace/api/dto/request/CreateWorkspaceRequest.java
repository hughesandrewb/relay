package com.andhug.relay.workspace.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateWorkspaceRequest(
        @NotBlank String name
) {}
