package com.andhug.relay.room.application.command;

import java.util.Optional;

import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;

import lombok.Builder;

@Builder
public record UpdateRoomCommand(
    RoomId roomId,
    ProfileId requesterId,
    Optional<String> name
) {}
