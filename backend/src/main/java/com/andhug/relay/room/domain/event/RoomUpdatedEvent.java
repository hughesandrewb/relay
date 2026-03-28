package com.andhug.relay.room.domain.event;

import org.jmolecules.event.annotation.DomainEvent;

import com.andhug.relay.shared.domain.model.RoomId;

@DomainEvent
public record RoomUpdatedEvent(
    RoomId roomId
) {}
