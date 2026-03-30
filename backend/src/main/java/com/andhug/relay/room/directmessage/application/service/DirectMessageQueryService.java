package com.andhug.relay.room.directmessage.application.service;

import com.andhug.relay.room.directmessage.application.mapper.DirectMessageMapper;
import com.andhug.relay.room.directmessage.domain.repository.DirectMessageRepository;
import com.andhug.relay.room.directmessage.infrastructure.web.dto.DirectMessageDto;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DirectMessageQueryService {

  private final DirectMessageRepository directMessageRepository;

  private final DirectMessageMapper directMessageMapper;

  public List<DirectMessageDto> getDirectMessages(ProfileId profileId) {

    return directMessageRepository.findByProfileId(profileId).stream()
        .map(directMessageMapper::toDto)
        .toList();
  }

  public DirectMessageDto getDirectMessageById(ProfileId profileId, RoomId roomId) {
    var directMessage = directMessageRepository.findById(roomId);

    if (!directMessage.getParticipants().contains(profileId)) {
      throw new IllegalArgumentException("Profile is not a participant of this direct message");
    }

    return directMessageMapper.toDto(directMessage);
  }
}
