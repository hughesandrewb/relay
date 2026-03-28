package com.andhug.relay.message.application.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.message.application.mapper.MessageMapper;
import com.andhug.relay.message.application.query.GetMessagesQuery;
import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.message.domain.repository.MessageRepository;
import com.andhug.relay.message.infrastructure.web.dto.MessageDto;
import com.andhug.relay.profile.application.service.ProfileQueryService;
import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageQueryService {

    private final MessageRepository messageRepository;

    private final ProfileQueryService profileQueryService;

    private final MessageMapper messageMapper;

    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(GetMessagesQuery query) {
        List<Message> messages = messageRepository.findMessages(query);

        List<ProfileId> authorIds = messages
            .stream()
            .map(Message::getAuthorId)
            .toList();

        List<ProfileDto> authors = profileQueryService
            .getProfiles(authorIds);

        Map<ProfileId, ProfileDto> authorMap = new HashMap<>();

        for (ProfileDto author : authors) {
            authorMap.put(ProfileId.of(author.id()), author);
        }

        return messages.stream()
            .map(message -> {
                return MessageDto.builder()
                    .id(message.getId().value())
                    .author(authorMap.get(message.getAuthorId()))
                    .content(message.getContent())
                    .roomId(message.getRoomId().value())
                    .build();
            })
            .toList();
    }

    @Transactional(readOnly = true)
    public MessageDto getMessage(MessageId messageId) {
        return messageMapper.toDto(messageRepository.findById(messageId));
    }
}
