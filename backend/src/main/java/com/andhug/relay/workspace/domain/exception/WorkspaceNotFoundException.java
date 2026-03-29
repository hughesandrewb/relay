package com.andhug.relay.workspace.domain.exception;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.WorkspaceId;

public class WorkspaceNotFoundException extends DomainException {
  public WorkspaceNotFoundException(WorkspaceId workspaceId) {
    super("Workspace not found with ID: " + workspaceId.toString());
  }
}
