package com.andhug.relay.workspace.application.service;

import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.application.mapper.WorkspaceMapper;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;
import com.andhug.relay.workspace.infrastructure.web.dto.WorkspaceDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class WorkspaceQueryService {

  private final WorkspaceRepository workspaceRepository;

  private final WorkspaceMapper workspaceMapper;

  @Transactional(readOnly = true)
  public WorkspaceDto getWorkspace(WorkspaceId workspaceId) {
    return workspaceMapper.toDto(workspaceRepository.findById(workspaceId));
  }
}
