package com.andhug.relay.workspace.domain.repository;

import java.util.UUID;

import com.andhug.relay.workspace.domain.model.Workspace;

public interface WorkspaceRepository {
    Workspace findById(UUID workspaceId);
    Workspace save(Workspace workspace);
}
