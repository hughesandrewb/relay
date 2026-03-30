package com.andhug.relay.room.application.handler;

import com.andhug.relay.room.application.command.UpdateRoomCommand;
import com.andhug.relay.room.domain.event.RoomUpdatedEvent;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.repository.RoomRepository;
import com.andhug.relay.shared.application.handler.SyncCommandHandler;
import com.andhug.relay.shared.domain.model.RoomId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateRoomHandler implements SyncCommandHandler<UpdateRoomCommand, RoomId> {

  private final RoomRepository roomRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  @Override
  public RoomId handle(UpdateRoomCommand command) {
    Room room = roomRepository.findById(command.roomId());

    if (command.name().isPresent()) {
      room.rename(command.name().get());
    }

    roomRepository.save(room);

    eventPublisher.publishEvent(new RoomUpdatedEvent(room.getId()));

    return room.getId();
  }
}
