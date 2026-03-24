package com.andhug.relay.workspace.domain.exception;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

public class InvalidWorkspaceNameException extends InvalidArgumentException {
    public InvalidWorkspaceNameException() {
        super("Workspace name cannot be null or blank");
    }
}
