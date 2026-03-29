package com.andhug.relay.workspacemembership.domain.repository;

import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspacemembership.domain.model.WorkspaceMember;
import java.util.Set;

public interface WorkspaceMemberRepository {
  void save(WorkspaceMember workspaceMember);

  Set<WorkspaceMember> findByWorkspaceId(WorkspaceId workspaceId);

  Set<WorkspaceMember> findByProfileId(ProfileId profileId);
}
