package com.andhug.relay.realtime.domain.model;

import com.andhug.relay.shared.domain.model.ProfileId;
import java.time.Duration;

public record RealtimeTicket(RealtimeTicketCode code, ProfileId profileId, Duration ttl) {

  public static RealtimeTicket of(ProfileId profileId, Duration ttl) {
    RealtimeTicketCode code = RealtimeTicketCode.generate();
    return new RealtimeTicket(code, profileId, ttl);
  }
}
