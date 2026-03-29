package com.andhug.relay.room.application.service;

import com.andhug.relay.room.application.command.CreateRoomCommand;
import com.andhug.relay.room.application.command.UpdateRoomCommand;
import com.andhug.relay.room.domain.event.RoomUpdatedEvent;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.model.RoomType;
import com.andhug.relay.room.domain.repository.RoomRepository;
import com.andhug.relay.shared.domain.model.RoomId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomCommandService {

  private final RoomRepository roomRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public RoomId updateRoom(UpdateRoomCommand command) {
    Room room = roomRepository.findById(command.roomId());

    if (command.name().isPresent()) {
      room.rename(command.name().get());
    }

    roomRepository.save(room);

    eventPublisher.publishEvent(new RoomUpdatedEvent(room.getId()));

    return room.getId();
  }

  @Transactional
  public RoomId createRoom(CreateRoomCommand command) {
    var room =
        Room.builder()
            .id(RoomId.generate())
            .workspaceId(command.workspaceId())
            .name(command.name())
            .type(RoomType.TEXT)
            .build();

    roomRepository.save(room);

    room.pullDomainEvents().forEach(eventPublisher::publishEvent);

    return room.getId();
  }
}
