package com.andhug.relay.room.application.command;

import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.RoomId;
import com.andhug.relay.shared.domain.model.WorkspaceId;

public record CreateRoomCommand(WorkspaceId workspaceId, String name)
    implements SyncCommand<RoomId> {

  public CreateRoomCommand {
    if (workspaceId == null) {
      throw new InvalidArgumentException("WorkspaceId cannot be null");
    }
    if (name == null) {
      throw new InvalidArgumentException("Name cannot be null");
    }
  }
}
