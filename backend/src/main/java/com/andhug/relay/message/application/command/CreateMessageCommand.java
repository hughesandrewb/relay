package com.andhug.relay.message.application.command;

import java.util.UUID;

import lombok.Builder;

@Builder
public record CreateMessageCommand(
    UUID authorId,
    UUID roomId,
    String content
) {}
