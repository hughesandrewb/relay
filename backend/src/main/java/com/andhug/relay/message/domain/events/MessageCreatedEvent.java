package com.andhug.relay.message.domain.events;

import java.util.UUID;

import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.shared.domain.event.DomainEvent;

import lombok.Getter;

@Getter
@org.jmolecules.event.annotation.DomainEvent
public class MessageCreatedEvent extends DomainEvent {

    private final MessageId messageId;

    private final UUID authorId;

    private final UUID roomId;

    private final String content;

    private MessageCreatedEvent(Message message) {
        this.messageId = message.getId();
        this.authorId = message.getAuthorId();
        this.roomId = message.getRoomId();
        this.content = message.getContent();
    }

    public static MessageCreatedEvent of(Message message) {
        return new MessageCreatedEvent(message);
    }
}
