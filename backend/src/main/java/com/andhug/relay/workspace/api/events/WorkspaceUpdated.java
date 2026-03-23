package com.andhug.relay.workspace.api.events;

import java.util.UUID;

import org.jmolecules.event.annotation.DomainEvent;

import com.andhug.relay.workspace.api.Workspace;

import lombok.Builder;

@DomainEvent
@Builder
public record WorkspaceUpdated(
    UUID workspaceId,
    Workspace workspace
) {}
