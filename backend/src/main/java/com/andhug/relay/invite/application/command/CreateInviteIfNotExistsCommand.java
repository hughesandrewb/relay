package com.andhug.relay.invite.application.command;

import com.andhug.relay.invite.domain.model.InviteId;
import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import lombok.Builder;

@Builder
public record CreateInviteIfNotExistsCommand(ProfileId senderId, WorkspaceId workspaceId)
    implements SyncCommand<InviteId> {}
