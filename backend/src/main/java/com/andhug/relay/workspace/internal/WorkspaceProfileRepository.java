package com.andhug.relay.workspace.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkspaceProfileRepository extends JpaRepository<WorkspaceProfileEntity, WorkspaceProfileKey> {

    boolean existsByWorkspaceIdAndProfileId(UUID workspaceId, UUID profileId);

    List<WorkspaceProfileEntity> findByWorkspace_Id(UUID workspaceId);
}
