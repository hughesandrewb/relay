package com.andhug.relay.realtime.domain.repository;

import com.andhug.relay.realtime.domain.model.RealtimeTicket;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.shared.domain.model.ProfileId;

public interface RealtimeTicketRepository {
    public ProfileId findByTicketCode(RealtimeTicketCode ticketCode);
    public void save(RealtimeTicket ticket);
}
