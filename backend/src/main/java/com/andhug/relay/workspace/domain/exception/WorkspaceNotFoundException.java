package com.andhug.relay.workspace.domain.exception;

import java.util.UUID;

import com.andhug.relay.shared.domain.exception.DomainException;

public class WorkspaceNotFoundException extends DomainException {
    public WorkspaceNotFoundException(UUID workspaceId) {
        super("Workspace not found with ID: " + workspaceId);
    }
}
