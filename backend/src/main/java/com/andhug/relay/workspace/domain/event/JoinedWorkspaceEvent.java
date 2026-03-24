package com.andhug.relay.workspace.domain.event;

import lombok.Builder;

import java.util.UUID;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
@Builder
public record JoinedWorkspaceEvent(
    UUID profileId,
    UUID workspaceId
) {}
