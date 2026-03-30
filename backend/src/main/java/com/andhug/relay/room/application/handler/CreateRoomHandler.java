package com.andhug.relay.room.application.handler;

import com.andhug.relay.room.application.command.CreateRoomCommand;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.model.RoomType;
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
public class CreateRoomHandler implements SyncCommandHandler<CreateRoomCommand, RoomId> {

  private final RoomRepository roomRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  @Override
  public RoomId handle(CreateRoomCommand command) {
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
