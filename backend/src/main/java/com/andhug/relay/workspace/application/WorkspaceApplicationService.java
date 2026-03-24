package com.andhug.relay.workspace.application;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.shared.domain.exception.UnauthorizedException;
import com.andhug.relay.workspace.application.command.CreateWorkspaceCommand;
import com.andhug.relay.workspace.application.command.UpdateWorkspaceCommand;
import com.andhug.relay.workspace.domain.event.WorkspaceCreatedEvent;
import com.andhug.relay.workspace.domain.event.WorkspaceUpdatedEvent;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkspaceApplicationService {

    private final WorkspaceRepository workspaceRepository;

    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional(readOnly = true)
    public Workspace getWorkspace(UUID id) {

        return workspaceRepository.findById(id);
    }

    @Transactional
    public Workspace createWorkspace(CreateWorkspaceCommand command) {

        Workspace workspace = Workspace.builder()
            .name(command.name())
            .ownerId(command.ownerId())
            .build();

        eventPublisher.publishEvent(new WorkspaceCreatedEvent(workspace.getId()));

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
