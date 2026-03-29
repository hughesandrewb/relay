package com.andhug.relay.room.directmessage.application.service;

import com.andhug.relay.room.directmessage.application.command.CreateDirectMessageCommand;
import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.room.directmessage.domain.repository.DirectMessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DirectMessageCommandService {

  private final DirectMessageRepository directMessageRepository;

  public DirectMessage createDirectMessage(CreateDirectMessageCommand command) {

    var directMessage = DirectMessage.builder().participants(command.participants()).build();

    directMessageRepository.save(directMessage);

    return directMessage;
  }
}
