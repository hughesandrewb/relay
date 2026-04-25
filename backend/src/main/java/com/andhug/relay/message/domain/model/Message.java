package com.andhug.relay.message.domain.model;

import com.andhug.relay.message.domain.events.MessageCreatedEvent;
import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.MessageId;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message extends AggregateRoot {

  private final MessageId id;

  private final ProfileId authorId;

  private final RoomId roomId;

  private String content;

  public static Message create(MessageId id, ProfileId authorId, RoomId roomId, String content) {
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
