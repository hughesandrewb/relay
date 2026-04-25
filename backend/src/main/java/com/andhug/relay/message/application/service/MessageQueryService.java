package com.andhug.relay.message.application.service;

import com.andhug.relay.message.application.mapper.MessageMapper;
import com.andhug.relay.message.application.query.GetMessagesQuery;
import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.domain.repository.MessageRepository;
import com.andhug.relay.message.infrastructure.web.dto.MessageDto;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.profile.domain.repository.ProfileRepository;
import com.andhug.relay.shared.domain.model.MessageId;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageQueryService {

  private final MessageRepository messageRepository;

  private final ProfileRepository profileRepository;

  private final MessageMapper messageMapper;

  @Transactional(readOnly = true)
  public List<MessageDto> getMessages(GetMessagesQuery query) {
    List<Message> messages = messageRepository.findMessages(query);

    List<ProfileId> authorIds = messages.stream().map(Message::getAuthorId).toList();

    List<Profile> authors = profileRepository.findAllById(authorIds);

    Map<ProfileId, Profile> authorMap = new HashMap<>();

    for (Profile author : authors) {
      authorMap.put(author.getId(), author);
    }

    return messages.stream()
        .map(
            message -> {
              Profile author = authorMap.get(message.getAuthorId());
              return messageMapper.toDto(message, author);
            })
        .toList();
  }

  @Transactional(readOnly = true)
  public MessageDto getMessage(MessageId messageId) {
    Message message = messageRepository.findById(messageId);
    Profile author = profileRepository.findById(message.getAuthorId());

    return messageMapper.toDto(message, author);
  }
}
