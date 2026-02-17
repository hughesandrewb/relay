package com.andhug.relay.message.api.events;

import com.andhug.relay.message.api.dto.MessageDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MessageCreated(
        MessageDto message,
        UUID roomId
) {}
