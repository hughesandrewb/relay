package com.andhug.relay.room.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.andhug.relay.room.application.mapper.RoomMapper;
import com.andhug.relay.room.domain.exception.RoomNotFoundException;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.repository.RoomRepository;
import com.andhug.relay.shared.domain.model.RoomId;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;

    private final RoomMapper roomMapper;

	@Override
	public Room findById(RoomId roomId) {
        return roomJpaRepository
            .findById(roomId.value())
            .map(roomMapper::toDomain)
            .orElseThrow(() -> new RoomNotFoundException(roomId));
	}
    
    @Override
    public void save(Room room) {
        RoomEntity roomEntity = roomMapper.toEntity(room);

        roomJpaRepository.save(roomEntity);
    }
}
