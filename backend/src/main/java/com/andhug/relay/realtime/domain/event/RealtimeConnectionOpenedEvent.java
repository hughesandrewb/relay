package com.andhug.relay.realtime.domain.event;

import com.andhug.relay.shared.domain.model.ProfileId;
import java.time.Instant;
import lombok.Builder;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
@Builder
public record RealtimeConnectionOpenedEvent(ProfileId profileId, Instant openedAt) {}
