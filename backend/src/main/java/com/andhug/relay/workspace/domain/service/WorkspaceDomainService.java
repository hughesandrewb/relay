package com.andhug.relay.workspace.domain.service;

import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileRepository;
import com.andhug.relay.workspace.application.mapper.WorkspaceMapper;
import com.andhug.relay.workspace.domain.event.JoinedWorkspaceEvent;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.infrastructure.persistence.WorkspaceEntity;
import com.andhug.relay.workspace.infrastructure.persistence.WorkspaceProfileEntity;
import com.andhug.relay.workspace.infrastructure.persistence.WorkspaceProfileKey;
import com.andhug.relay.workspace.infrastructure.persistence.WorkspaceProfileJpaRepository;
import com.andhug.relay.workspace.infrastructure.persistence.WorkspaceJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceDomainService {

    private final WorkspaceJpaRepository workspaceRepository;

    private final WorkspaceProfileJpaRepository workspaceProfileRepository;

    private final ProfileRepository profileRepository;

    private final WorkspaceMapper workspaceMapper;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<Workspace> findAllByProfileId(UUID profileId) {

        return workspaceRepository.findByMembers_ProfileId(profileId)
            .stream()
            .map(workspaceMapper::toDomain)
            .toList();
    }

    @Transactional
    public void joinWorkspace(UUID profileId, UUID workspaceId) {

        WorkspaceEntity workspaceEntity = workspaceRepository.getReferenceById(workspaceId);
        ProfileEntity profileEntity = profileRepository.getReferenceById(profileId);

        WorkspaceProfileEntity workspaceProfileEntity = WorkspaceProfileEntity.builder()
            .id(WorkspaceProfileKey.builder()
                .profileId(profileEntity.getId())
                .workspaceId(workspaceEntity.getId())
                .build())
            .profile(profileEntity)
            .workspace(workspaceEntity)
            .build();

        eventPublisher.publishEvent(JoinedWorkspaceEvent.builder()
            .profileId(profileId)
            .workspaceId(workspaceId)
            .build());

        workspaceProfileRepository.save(workspaceProfileEntity);
    }

    @Transactional(readOnly = true)
    public List<UUID> getMemberIds(UUID workspaceId) {

        return workspaceProfileRepository.findByWorkspace_Id(workspaceId)
            .stream()
            .map(wp -> wp.getProfile().getId())
            .toList();
    }

}
