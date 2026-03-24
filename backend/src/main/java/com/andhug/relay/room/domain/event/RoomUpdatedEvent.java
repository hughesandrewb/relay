package com.andhug.relay.room.domain.event;

import java.util.UUID;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record RoomUpdatedEvent(
    UUID roomId
) {}
