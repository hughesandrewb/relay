package com.andhug.relay.message.api;

import com.andhug.relay.id.SnowflakeGenerator;
import com.andhug.relay.message.api.events.MessageCreated;
import com.andhug.relay.message.api.dto.CreateMessageRequest;
import com.andhug.relay.message.api.dto.MessageDto;
import com.andhug.relay.message.internal.MessageEntity;
import com.andhug.relay.message.internal.MessageRepository;
import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileDto;
import com.andhug.relay.profile.ProfileService;
import com.andhug.relay.profile.internal.ProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final SnowflakeGenerator snowflakeGenerator;

    private final ProfileMapper profileMapper;

    private final ProfileService profileService;

    @Transactional
    public MessageDto createMessage(CreateMessageRequest request, Profile author) {

        var toSave = new MessageEntity();

        toSave.setId(snowflakeGenerator.nextId());
        toSave.setAuthorId(author.getId());
        toSave.setRoomId(request.roomId());
        toSave.setContent(request.content());

        MessageEntity saved = messageRepository.save(toSave);

        ProfileDto authorDto = profileMapper.toDto(author);

        var messageDto = MessageDto.builder()
                .id(saved.getId())
                .author(authorDto)
                .roomId(saved.getRoomId())
                .content(saved.getContent())
                .build();

        eventPublisher.publishEvent(MessageCreated.builder()
                .message(messageDto)
                .roomId(saved.getRoomId())
                .build());

        return messageDto;
    }

    public List<MessageDto> getMessages(UUID roomId) {

        return getMessages(roomId, 0, 50);
    }

    public List<MessageDto> getMessages(UUID roomId, int limit) {

        return getMessages(roomId, 0, limit);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(UUID roomId, int offset, int limit) {

        int pageSize = limit;
        int pageNumber = offset / limit;

        return messageRepository
                .findByRoomId(roomId, PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(messageEntity -> {
                    Profile author = profileService.getProfile(messageEntity.getAuthorId());
                    ProfileDto authorDto = profileMapper.toDto(author);
                    return new MessageDto(messageEntity.getId(), authorDto, messageEntity.getRoomId(),
                            messageEntity.getContent());
                })
                .toList();
    }
}
