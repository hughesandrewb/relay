package com.andhug.relay.room.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.room.application.mapper.RoomMapper;
import com.andhug.relay.room.domain.repository.RoomRepository;
import com.andhug.relay.room.infrastructure.web.dto.RoomDto;
import com.andhug.relay.shared.domain.model.RoomId;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RoomQueryService {
    
    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public RoomDto getRoom(RoomId roomId) {

        return roomMapper.toDto(roomRepository.findById(roomId));
    }
}
