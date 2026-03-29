package com.andhug.relay.realtime.application.command;

import com.andhug.relay.shared.domain.model.ProfileId;
import java.time.Duration;
import lombok.Builder;

@Builder
public record CreateRealtimeTicketCommand(ProfileId profileId, Duration ttlDuration) {

  public static final int REALTIME_TICKET_DURATION_SECONDS = 30;

  public static CreateRealtimeTicketCommand of(ProfileId profileId) {
    return new CreateRealtimeTicketCommand(
        profileId, Duration.ofSeconds(REALTIME_TICKET_DURATION_SECONDS));
  }

  public static CreateRealtimeTicketCommand of(ProfileId profileId, Duration ttlDuration) {
    return new CreateRealtimeTicketCommand(profileId, ttlDuration);
  }
}
