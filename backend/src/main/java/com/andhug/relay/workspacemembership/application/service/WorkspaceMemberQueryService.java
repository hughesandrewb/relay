package com.andhug.relay.workspacemembership.application.service;

import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.application.mapper.WorkspaceMapper;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;
import com.andhug.relay.workspace.infrastructure.web.dto.WorkspaceDto;
import com.andhug.relay.workspacemembership.domain.model.WorkspaceMember;
import com.andhug.relay.workspacemembership.domain.repository.WorkspaceMemberRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class WorkspaceMemberQueryService {

  private final WorkspaceMemberRepository workspaceMemberRepository;

  private final WorkspaceRepository workspaceRepository;

  private final WorkspaceMapper workspaceMapper;

  public Set<ProfileId> getMembersOfWorkspace(WorkspaceId workspaceId) {
    return workspaceMemberRepository.findByWorkspaceId(workspaceId).stream()
        .map(WorkspaceMember::getProfileId)
        .collect(Collectors.toSet());
  }

  public Set<WorkspaceId> getWorkspaceIdsOfProfile(ProfileId profileId) {
    return workspaceMemberRepository.findByProfileId(profileId).stream()
        .map(WorkspaceMember::getWorkspaceId)
        .collect(Collectors.toSet());
  }

  public List<WorkspaceDto> getWorkspacesOfProfile(ProfileId profileId) {
    List<WorkspaceId> workspaceIds =
        workspaceMemberRepository.findByProfileId(profileId).stream()
            .map(WorkspaceMember::getWorkspaceId)
            .toList();

    return workspaceRepository.findAllById(workspaceIds).stream()
        .map(workspaceMapper::toDto)
        .toList();
  }
}
