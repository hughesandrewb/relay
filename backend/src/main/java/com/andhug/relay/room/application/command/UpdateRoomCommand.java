package com.andhug.relay.room.application.command;

import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import java.util.Optional;
import lombok.Builder;

@Builder
public record UpdateRoomCommand(RoomId roomId, ProfileId requesterId, Optional<String> name)
    implements SyncCommand<RoomId> {}
