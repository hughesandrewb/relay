package com.andhug.relay.gateway.application.service;

import com.andhug.relay.gateway.domain.exception.InvalidRealtimeTicketException;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.realtime.domain.repository.RealtimeTicketRepository;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealtimeTicketService {

  private final RealtimeTicketRepository realtimeTicketRepository;

  public ProfileId validateTicket(RealtimeTicketCode ticketCode) {
    ProfileId profileId;

    try {
      profileId = realtimeTicketRepository.findByTicketCode(ticketCode);
    } catch (Exception e) {
      throw new InvalidRealtimeTicketException(ticketCode);
    }
    realtimeTicketRepository.delete(ticketCode);

    return profileId;
  }
}
