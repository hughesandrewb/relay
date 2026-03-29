package com.andhug.relay.workspacemembership.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceMemberJpaRepository extends JpaRepository<WorkspaceMemberEntity, UUID> {
  boolean existsByWorkspaceIdAndProfileId(UUID workspaceId, UUID profileId);

  List<WorkspaceMemberEntity> findByWorkspaceId(UUID workspaceId);

  List<WorkspaceMemberEntity> findByProfileId(UUID profileId);
}
