package com.andhug.relay.workspace.api;

import com.andhug.relay.profile.ProfileContext;
import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileRepository;
import com.andhug.relay.workspace.api.events.JoinedWorkspaceEvent;
import com.andhug.relay.workspace.api.events.WorkspaceCreatedEvent;
import com.andhug.relay.workspace.internal.*;
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
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceProfileRepository workspaceProfileRepository;

    private final ProfileRepository profileRepository;

    private final WorkspaceMapper workspaceMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Workspace createWorkspace(Workspace workspace) {

        WorkspaceEntity workspaceEntity = workspaceMapper.toEntity(workspace);

        ProfileEntity profileEntity = profileRepository.getReferenceById(ProfileContext.getCurrentProfile().getId());

        workspaceEntity.setOwnerId(profileEntity.getId());

        workspaceRepository.save(workspaceEntity);

        var workspaceProfileEntity = WorkspaceProfileEntity.builder()
                .id(WorkspaceProfileKey.builder()
                        .profileId(profileEntity.getId())
                        .workspaceId(workspaceEntity.getId())
                        .build())
                .profile(profileEntity)
                .workspace(workspaceEntity)
                .build();

        workspaceProfileRepository.save(workspaceProfileEntity);

        applicationEventPublisher.publishEvent(WorkspaceCreatedEvent.builder()
                .name(workspaceEntity.getName())
                .workspaceId(workspaceEntity.getId())
                .build());

        Workspace res = workspaceMapper.toDomain(workspaceEntity);
        res.setOwnerId(profileEntity.getId());

        return res;
    }

    @Transactional(readOnly = true)
    public List<Workspace> findAllByProfileId(UUID profileId) {

        return workspaceRepository.findByMembers_ProfileId(profileId)
                .stream()
                .map(workspaceMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public Workspace findById(UUID id) {

        return workspaceRepository.findById(id)
                .map(workspaceMapper::toDomain)
                .get();
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

        applicationEventPublisher.publishEvent(JoinedWorkspaceEvent.builder()
                .profileId(profileId)
                .workspaceId(workspaceId)
                .build());

        workspaceProfileRepository.save(workspaceProfileEntity);
    }
}
