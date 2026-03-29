package com.andhug.relay.message.domain.events;

import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.shared.domain.event.DomainEvent;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@org.jmolecules.event.annotation.DomainEvent
@AllArgsConstructor
public class MessageCreatedEvent extends DomainEvent {

  private final MessageId messageId;

  private final ProfileId authorId;

  private final RoomId roomId;

  private final String content;

  public static MessageCreatedEvent of(Message message) {
    return new MessageCreatedEvent(
        message.getId(), message.getAuthorId(), message.getRoomId(), message.getContent());
  }
}
