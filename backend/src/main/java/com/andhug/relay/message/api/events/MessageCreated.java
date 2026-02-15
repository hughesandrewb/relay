package com.andhug.relay.message.api.events;

import java.util.UUID;

public record MessageCreated(
        long id,
        UUID authorId,
        UUID roomId,
        String content
) {}
