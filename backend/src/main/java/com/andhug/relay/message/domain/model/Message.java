package com.andhug.relay.message.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

import com.andhug.relay.message.domain.events.MessageCreatedEvent;
import com.andhug.relay.shared.domain.model.AggregateRoot;

@Getter
@AllArgsConstructor
public class Message extends AggregateRoot {

    private final MessageId id;

    private final UUID authorId;

    private final UUID roomId;

    private String content;

    public static Message create(MessageId id, UUID authorId, UUID roomId, String content) {
        if (authorId == null) {
            throw new IllegalArgumentException("authorId cannot be null");
        }
        if (roomId == null) {
            throw new IllegalArgumentException("roomId cannot be null");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content cannot be null or blank");
        }

        var message = new Message(id, authorId, roomId, content);
        message.registerEvent(MessageCreatedEvent.of(message));

        return message;
    }
}
