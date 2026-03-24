package com.andhug.relay.workspace.domain.event;

import java.util.UUID;

import org.jmolecules.event.annotation.DomainEvent;

import lombok.Builder;

@DomainEvent
@Builder
public record WorkspaceUpdatedEvent(
    UUID workspaceId
) {}
