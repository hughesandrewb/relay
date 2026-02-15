package com.andhug.relay.workspace.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, UUID> {

    /**
     * Finds all workspaces where the specified profile is a member.
     * Queries through the workspace's members collection to find matches
     *
     * @param profileId the UUID of the profile
     * @return list of workspaces containing the profile as a member, empty list if none
     */
    List<WorkspaceEntity> findByMembers_ProfileId(@Param("profileId") UUID profileId);
}
