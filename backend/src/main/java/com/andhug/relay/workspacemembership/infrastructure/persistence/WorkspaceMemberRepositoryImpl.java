package com.andhug.relay.workspacemembership.infrastructure.persistence;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspacemembership.application.mapper.WorkspaceMemberMapper;
import com.andhug.relay.workspacemembership.domain.model.WorkspaceMember;
import com.andhug.relay.workspacemembership.domain.repository.WorkspaceMemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WorkspaceMemberRepositoryImpl implements WorkspaceMemberRepository {

    private final WorkspaceMemberJpaRepository workspaceMemberJpaRepository;

    private final WorkspaceMemberMapper workspaceMemberMapper;

	@Override
	public void save(WorkspaceMember workspaceMember) {
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberMapper.toEntity(workspaceMember);

        workspaceMemberJpaRepository.save(workspaceMemberEntity);
	}

	@Override
	public Set<WorkspaceMember> findByWorkspaceId(WorkspaceId workspaceId) {
        return workspaceMemberJpaRepository
            .findByWorkspaceId(workspaceId.value())
            .stream()
            .map(workspaceMemberMapper::toDomain)
            .collect(Collectors.toSet());
	}

	@Override
	public Set<WorkspaceMember> findByProfileId(ProfileId profileId) {
        return workspaceMemberJpaRepository
            .findByProfileId(profileId.value())
            .stream()
            .map(workspaceMemberMapper::toDomain)
            .collect(Collectors.toSet());
	}
    
}
