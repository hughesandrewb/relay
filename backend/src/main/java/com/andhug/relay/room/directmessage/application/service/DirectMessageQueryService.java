package com.andhug.relay.room.directmessage.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.andhug.relay.room.directmessage.application.mapper.DirectMessageMapper;
import com.andhug.relay.room.directmessage.domain.repository.DirectMessageRepository;
import com.andhug.relay.room.directmessage.infrastructure.web.dto.DirectMessageDto;
import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DirectMessageQueryService {
    
    private final DirectMessageRepository directMessageRepository;

    private final DirectMessageMapper directMessageMapper;

    public List<DirectMessageDto> getDirectMessage(ProfileId profileId) {
        
        return directMessageRepository
            .findByProfileId(profileId)
            .stream()
            .map(directMessageMapper::toDto)
            .toList();
    }
}
