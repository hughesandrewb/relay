package com.andhug.relay.workspace.domain.repository;

import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.domain.model.Workspace;
import java.util.List;

public interface WorkspaceRepository {
  Workspace findById(WorkspaceId workspaceId);

  List<Workspace> findAllById(List<WorkspaceId> workspaceIds);

  Workspace save(Workspace workspace);
}
