package com.andhug.relay.workspace.infrastructure.persistence;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.andhug.relay.workspace.application.mapper.WorkspaceMapper;
import com.andhug.relay.workspace.domain.exception.WorkspaceNotFoundException;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJpaRepository workspaceJpaRepository;

    private final WorkspaceMapper workspaceMapper;

	@Override
	public Workspace findById(UUID workspaceId) {

        return workspaceJpaRepository
            .findById(workspaceId)
            .map(workspaceMapper::toDomain)
            .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
	}

	@Override
	public Workspace save(Workspace workspace) {

        WorkspaceEntity workspaceEntity = workspaceMapper.toEntity(workspace);

        workspaceJpaRepository.save(workspaceEntity);

        return workspaceMapper.toDomain(workspaceEntity);
	}
}
