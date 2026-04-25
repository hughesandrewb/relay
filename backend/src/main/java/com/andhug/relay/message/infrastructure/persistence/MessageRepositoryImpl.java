package com.andhug.relay.message.infrastructure.persistence;

import com.andhug.relay.message.application.mapper.MessageMapper;
import com.andhug.relay.message.application.query.GetMessagesQuery;
import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.domain.repository.MessageRepository;
import com.andhug.relay.shared.domain.exception.NotFoundException;
import com.andhug.relay.shared.domain.model.MessageId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

  private final MessageJpaRepository messageJpaRepository;

  private final MessageMapper messageMapper;

  @Override
  public Message findById(MessageId messageId) {
    return messageMapper.toDomain(
        messageJpaRepository
            .findById(messageId.value())
            .orElseThrow(() -> new NotFoundException("Could not find Message")));
  }

  @Override
  public List<Message> findMessages(GetMessagesQuery query) {
    return messageJpaRepository.findByRoomId(query.roomId()).stream()
        .map(messageMapper::toDomain)
        .toList();
  }

  @Override
  public void save(Message message) {
    MessageEntity messageEntity = messageMapper.toEntity(message);

    messageJpaRepository.save(messageEntity);
  }
}
