package com.andhug.relay.workspace.application.command;

import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import java.util.Optional;
import lombok.Builder;

@Builder
public record UpdateWorkspaceCommand(
    WorkspaceId workspaceId,
    ProfileId requesterId,
    Optional<String> name,
    Optional<ProfileId> ownerId)
    implements SyncCommand<WorkspaceId> {}
