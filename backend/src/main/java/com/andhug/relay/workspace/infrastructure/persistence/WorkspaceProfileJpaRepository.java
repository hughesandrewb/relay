package com.andhug.relay.workspace.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkspaceProfileJpaRepository extends JpaRepository<WorkspaceProfileEntity, WorkspaceProfileKey> {

    boolean existsByWorkspaceIdAndProfileId(UUID workspaceId, UUID profileId);

    List<WorkspaceProfileEntity> findByWorkspace_Id(UUID workspaceId);
}
