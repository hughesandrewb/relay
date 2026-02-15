package com.andhug.relay.workspace.api.events;

import lombok.Builder;
import org.jmolecules.event.annotation.DomainEvent;

import java.util.UUID;

@DomainEvent
@Builder
public record WorkspaceCreatedEvent(
        UUID workspaceId,
        String name
) {}
