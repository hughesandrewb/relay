package com.andhug.relay.workspace.application.command;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CreateWorkspaceCommand(
    UUID ownerId,
    String name
) {}
