package com.andhug.relay.workspace.application.service;

import com.andhug.relay.shared.domain.exception.UnauthorizedException;
import com.andhug.relay.workspace.application.command.CreateWorkspaceCommand;
import com.andhug.relay.workspace.application.command.UpdateWorkspaceCommand;
import com.andhug.relay.workspace.domain.event.WorkspaceUpdatedEvent;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceCommandService {

  private final WorkspaceRepository workspaceRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public Workspace createWorkspace(CreateWorkspaceCommand command) {

    Workspace workspace = Workspace.create(command.name(), command.ownerId());

    workspace.pullDomainEvents().forEach(eventPublisher::publishEvent);

    return workspaceRepository.save(workspace);
  }

  @Transactional
  public Workspace updateWorkspace(UpdateWorkspaceCommand command) {

    Workspace workspace = workspaceRepository.findById(command.workspaceId());

    if (!workspace.isOwnedBy(command.requesterId())) {
      throw new UnauthorizedException("Only the owner can update the workspace");
    }

    if (command.name().isPresent()) {
      workspace.rename(command.name().get());
    }
    if (command.ownerId().isPresent()) {
      workspace.changeOwner(command.ownerId().get());
    }

    eventPublisher.publishEvent(new WorkspaceUpdatedEvent(workspace.getId()));

    return workspaceRepository.save(workspace);
  }
}
