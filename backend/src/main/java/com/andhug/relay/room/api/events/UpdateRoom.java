package com.andhug.relay.room.api.events;

import java.util.UUID;

import org.jmolecules.event.annotation.DomainEvent;

import com.andhug.relay.room.api.Room;

import lombok.Builder;

@DomainEvent
@Builder
public record UpdateRoom(
    UUID roomId,
    Room room
) {}
