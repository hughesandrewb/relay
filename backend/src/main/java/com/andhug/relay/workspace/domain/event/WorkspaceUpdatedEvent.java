package com.andhug.relay.workspace.domain.event;

import com.andhug.relay.shared.domain.model.WorkspaceId;
import lombok.Builder;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
@Builder
public record WorkspaceUpdatedEvent(WorkspaceId workspaceId) {}
