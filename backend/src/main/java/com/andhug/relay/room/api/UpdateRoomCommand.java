package com.andhug.relay.room.api;

import java.util.UUID;

public record UpdateRoomCommand(
    UUID roomId,
    String name
) {}
