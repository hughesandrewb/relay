package com.andhug.relay.workspace.application.command;

import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.Builder;

@Builder
public record CreateWorkspaceCommand(
    ProfileId ownerId,
    String name
) {}
