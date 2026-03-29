package com.andhug.relay.workspace.domain.service;

import com.andhug.relay.workspace.domain.event.JoinedWorkspaceEvent;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspacemembership.infrastructure.persistence.WorkspaceMemberEntity;
import com.andhug.relay.workspacemembership.infrastructure.persistence.WorkspaceMemberJpaRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceDomainService {

  private final WorkspaceMemberJpaRepository workspaceProfileRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional(readOnly = true)
  public List<Workspace> findAllByProfileId(UUID profileId) {

    // return workspaceRepository.findByMembers_ProfileId(profileId)
    //     .stream()
    //     .map(workspaceMapper::toDomain)
    //     .toList();

    return null;
  }

  @Transactional
  public void joinWorkspace(UUID profileId, UUID workspaceId) {

    WorkspaceMemberEntity workspaceProfileEntity =
        WorkspaceMemberEntity.builder().profileId(profileId).workspaceId(workspaceId).build();

    eventPublisher.publishEvent(
        JoinedWorkspaceEvent.builder().profileId(profileId).workspaceId(workspaceId).build());

    workspaceProfileRepository.save(workspaceProfileEntity);
  }

  @Transactional(readOnly = true)
  public List<UUID> getMemberIds(UUID workspaceId) {

    return workspaceProfileRepository.findByWorkspaceId(workspaceId).stream()
        .map(wp -> wp.getProfileId())
        .toList();
  }
}
