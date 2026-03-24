package com.andhug.relay.workspace.domain.exception;

import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

public class InvalidWorkspaceOwnerException extends InvalidArgumentException {
    public InvalidWorkspaceOwnerException() {
        super("Workspace owner cannot be null");
    }
}
