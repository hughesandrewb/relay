package com.andhug.relay.workspacemembership.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkspaceMemberJpaRepository extends JpaRepository<WorkspaceMemberEntity, UUID> {
    boolean existsByWorkspaceIdAndProfileId(UUID workspaceId, UUID profileId);
    List<WorkspaceMemberEntity> findByWorkspaceId(UUID workspaceId);
    List<WorkspaceMemberEntity> findByProfileId(UUID profileId);
}
