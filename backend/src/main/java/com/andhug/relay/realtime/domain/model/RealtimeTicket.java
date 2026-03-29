package com.andhug.relay.realtime.domain.model;

import java.time.Duration;

import com.andhug.relay.shared.domain.model.ProfileId;

public record RealtimeTicket(
    RealtimeTicketCode code,
    ProfileId profileId,
    Duration ttl
) {

    public static RealtimeTicket of(ProfileId profileId, Duration ttl) {
        RealtimeTicketCode code = RealtimeTicketCode.generate();
        return new RealtimeTicket(code, profileId, ttl);
    }
}
