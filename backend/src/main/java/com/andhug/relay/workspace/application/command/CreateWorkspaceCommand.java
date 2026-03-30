package com.andhug.relay.workspace.application.command;

import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import lombok.Builder;

@Builder
public record CreateWorkspaceCommand(ProfileId ownerId, String name)
    implements SyncCommand<WorkspaceId> {}
