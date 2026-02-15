package com.andhug.relay.workspace.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceProfileRepository extends JpaRepository<WorkspaceProfileEntity, UUID> {

    boolean existsByWorkspaceIdAndProfileId(UUID workspaceId, UUID profileId);
}
