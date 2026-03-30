package com.andhug.relay.room.directmessage.application.handler;

import com.andhug.relay.room.directmessage.application.command.CreateDirectMessageCommand;
import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.room.directmessage.domain.repository.DirectMessageRepository;
import com.andhug.relay.shared.application.handler.SyncCommandHandler;
import com.andhug.relay.shared.domain.model.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateDirectMessageHandler
    implements SyncCommandHandler<CreateDirectMessageCommand, RoomId> {

  private final DirectMessageRepository directMessageRepository;

  @Override
  @Transactional
  public RoomId handle(CreateDirectMessageCommand command) {
    var directMessage = DirectMessage.create(command.participants());

    directMessageRepository.save(directMessage);

    return directMessage.getId();
  }
}
