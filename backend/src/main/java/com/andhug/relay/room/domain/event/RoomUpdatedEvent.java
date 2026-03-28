package com.andhug.relay.room.domain.event;

import com.andhug.relay.shared.domain.event.DomainEvent;
import com.andhug.relay.shared.domain.model.RoomId;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@org.jmolecules.event.annotation.DomainEvent
public class RoomUpdatedEvent extends DomainEvent {

    private RoomId roomId;
}
