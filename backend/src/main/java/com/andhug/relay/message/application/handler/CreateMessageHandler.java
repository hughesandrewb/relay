package com.andhug.relay.message.application.handler;

import com.andhug.relay.id.SnowflakeGenerator;
import com.andhug.relay.message.application.command.CreateMessageCommand;
import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.message.domain.repository.MessageRepository;
import com.andhug.relay.shared.application.handler.SyncCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMessageHandler implements SyncCommandHandler<CreateMessageCommand, MessageId> {

  private final MessageRepository messageRepository;

  private final SnowflakeGenerator snowflakeGenerator;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public MessageId handle(CreateMessageCommand command) {
    var message =
        Message.create(
            MessageId.of(snowflakeGenerator.nextId()),
            command.authorId(),
            command.roomId(),
            command.content());

    messageRepository.save(message);

    message.pullDomainEvents().forEach(eventPublisher::publishEvent);

    return message.getId();
  }
}
