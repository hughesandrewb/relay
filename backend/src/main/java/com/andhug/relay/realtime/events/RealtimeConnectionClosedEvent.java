package com.andhug.relay.realtime.events;

import lombok.Builder;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@DomainEvent
@Builder
public record RealtimeConnectionClosedEvent(
        UUID profileId,
        Instant closedAt
) {}
