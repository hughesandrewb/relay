package com.andhug.relay.workspace.infrastructure.persistence;

import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.application.mapper.WorkspaceMapper;
import com.andhug.relay.workspace.domain.exception.WorkspaceNotFoundException;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

  private final WorkspaceJpaRepository workspaceJpaRepository;

  private final WorkspaceMapper workspaceMapper;

  @Override
  public Workspace findById(WorkspaceId workspaceId) {

    return workspaceJpaRepository
        .findById(workspaceId.value())
        .map(workspaceMapper::toDomain)
        .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
  }

  @Override
  public List<Workspace> findAllById(List<WorkspaceId> workspaceIds) {

    List<UUID> ids = workspaceIds.stream().map(WorkspaceId::value).toList();

    return workspaceJpaRepository.findAllById(ids).stream().map(workspaceMapper::toDomain).toList();
  }

  @Override
  public Workspace save(Workspace workspace) {

    WorkspaceEntity workspaceEntity = workspaceMapper.toEntity(workspace);

    workspaceJpaRepository.save(workspaceEntity);

    return workspaceMapper.toDomain(workspaceEntity);
  }
}
