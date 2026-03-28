package com.andhug.relay.workspace.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import com.andhug.relay.shared.domain.model.WorkspaceId;

import lombok.Builder;

@DomainEvent
@Builder
public record WorkspaceUpdatedEvent(
    WorkspaceId workspaceId
) {}
