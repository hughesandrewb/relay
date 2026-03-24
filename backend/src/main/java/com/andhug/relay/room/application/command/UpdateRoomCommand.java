package com.andhug.relay.room.application.command;

import java.util.Optional;
import java.util.UUID;

import lombok.Builder;

@Builder
public record UpdateRoomCommand(
    UUID roomId,
    UUID requesterId,
    Optional<String> name
) {}
