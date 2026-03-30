package com.andhug.relay.workspace.application.handler;

import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.model.RoomType;
import com.andhug.relay.room.domain.repository.RoomRepository;
import com.andhug.relay.shared.application.handler.SyncCommandHandler;
import com.andhug.relay.shared.domain.model.WorkspaceId;
import com.andhug.relay.workspace.application.command.CreateWorkspaceCommand;
import com.andhug.relay.workspace.domain.model.Workspace;
import com.andhug.relay.workspace.domain.repository.WorkspaceRepository;
import com.andhug.relay.workspacemembership.domain.model.WorkspaceMember;
import com.andhug.relay.workspacemembership.domain.repository.WorkspaceMemberRepository;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class CreateWorkspaceHandler
    implements SyncCommandHandler<CreateWorkspaceCommand, WorkspaceId> {

  private final WorkspaceRepository workspaceRepository;

  private final WorkspaceMemberRepository workspaceMemberRepository;

  private final RoomRepository roomRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public WorkspaceId handle(CreateWorkspaceCommand command) {

    Workspace workspace = Workspace.create(command.name(), command.ownerId());

    WorkspaceMember workspaceMember = WorkspaceMember.create(workspace.getId(), command.ownerId());

    Room room = Room.create("general", workspace.getId(), RoomType.TEXT);

    workspaceRepository.save(workspace);
    workspaceMemberRepository.save(workspaceMember);
    roomRepository.save(room);

    Stream.of(workspace, workspaceMember, room)
        .flatMap(aggregate -> aggregate.pullDomainEvents().stream())
        .forEach(eventPublisher::publishEvent);

    return workspace.getId();
  }
}
