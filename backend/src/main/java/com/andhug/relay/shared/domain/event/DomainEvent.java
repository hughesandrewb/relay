package com.andhug.relay.shared.domain.event;

import java.time.Instant;

public class DomainEvent {

  private final Instant occurredAt = Instant.now();

  public Instant getOccurredAt() {
    return occurredAt;
  }
}
