package com.andhug.relay.workspace.application.command;

import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

@Builder
public record UpdateWorkspaceCommand(
    UUID workspaceId,
    UUID requesterId,
    Optional<String> name,
    Optional<UUID> ownerId
) {}
