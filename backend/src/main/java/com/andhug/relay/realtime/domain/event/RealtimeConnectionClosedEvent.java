package com.andhug.relay.realtime.domain.event;

import com.andhug.relay.shared.domain.event.DomainEvent;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@org.jmolecules.event.annotation.DomainEvent
@AllArgsConstructor
public class RealtimeConnectionClosedEvent extends DomainEvent {

  private final ProfileId profileId;

  private final Instant closedAt;

  public static RealtimeConnectionClosedEvent of(ProfileId profileId) {
    return new RealtimeConnectionClosedEvent(profileId, Instant.now());
  }
}
