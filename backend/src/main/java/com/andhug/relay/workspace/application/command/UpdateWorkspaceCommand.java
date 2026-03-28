package com.andhug.relay.workspace.application.command;

import java.util.Optional;

import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;

import lombok.Builder;

@Builder
public record UpdateWorkspaceCommand(
    WorkspaceId workspaceId,
    ProfileId requesterId,
    Optional<String> name,
    Optional<ProfileId> ownerId
) {}
