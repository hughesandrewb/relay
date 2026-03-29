package com.andhug.relay.realtime.domain.event;

import lombok.Builder;
import org.jmolecules.event.annotation.DomainEvent;

import com.andhug.relay.shared.domain.model.ProfileId;

import java.time.Instant;

@DomainEvent
@Builder
public record RealtimeConnectionOpenedEvent(
        ProfileId profileId,
        Instant openedAt
) {}
