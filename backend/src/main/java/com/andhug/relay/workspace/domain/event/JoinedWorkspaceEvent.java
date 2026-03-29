package com.andhug.relay.workspace.domain.event;

import java.util.UUID;
import lombok.Builder;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
@Builder
public record JoinedWorkspaceEvent(UUID profileId, UUID workspaceId) {}
