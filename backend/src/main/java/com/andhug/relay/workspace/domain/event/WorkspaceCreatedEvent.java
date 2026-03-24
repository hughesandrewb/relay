package com.andhug.relay.workspace.domain.event;

import lombok.Builder;
import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
@Builder
public record WorkspaceCreatedEvent(
    UUID workspaceId
) {}
