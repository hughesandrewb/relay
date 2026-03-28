package com.andhug.relay.room.application.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.room.application.command.UpdateRoomCommand;
import com.andhug.relay.room.domain.event.RoomUpdatedEvent;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomCommandService {

    private final RoomRepository roomRepository;

    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public Room updateRoom(UpdateRoomCommand command) {
        Room room = roomRepository.findById(command.roomId());

        if (command.name().isPresent()) {
            room.rename(command.name().get());
        }

        Room updated = roomRepository.save(room);

        eventPublisher.publishEvent(new RoomUpdatedEvent(updated.getId()));

        return updated;
    }
}
