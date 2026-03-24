package com.andhug.relay.room.infrastructure.persistence;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.andhug.relay.room.application.mapper.RoomMapper;
import com.andhug.relay.room.domain.exception.RoomNotFoundException;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;

    private final RoomMapper roomMapper;

	@Override
	public Room findById(UUID roomId) {
        return roomJpaRepository
            .findById(roomId)
            .map(roomMapper::toDomain)
            .orElseThrow(() -> new RoomNotFoundException(roomId));
	}
    
    @Override
    public Room save(Room room) {
        RoomEntity roomEntity = roomMapper.toEntity(room);

        roomJpaRepository.save(roomEntity);

        return roomMapper.toDomain(roomEntity);
    }
}
