package com.andhug.relay.workspace.api.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JoinedWorkspaceEvent(
        UUID profileId,
        UUID workspaceId
) {}
