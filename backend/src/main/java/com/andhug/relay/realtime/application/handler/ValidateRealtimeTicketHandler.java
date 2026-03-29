package com.andhug.relay.realtime.application.handler;

import com.andhug.relay.realtime.application.command.ValidateRealtimeTicketCommand;
import com.andhug.relay.realtime.domain.repository.RealtimeTicketRepository;
import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateRealtimeTicketHandler {

  private final RealtimeTicketRepository realtimeTicketRepository;

  public ProfileId handle(ValidateRealtimeTicketCommand command) {

    log.info("Validating ticket code: {}", command.ticketCode().toString());

    try {
      return realtimeTicketRepository.findByTicketCode(command.ticketCode());
    } catch (InvalidArgumentException e) {
      log.error("Invalid profileId for ticket: {}", command.ticketCode().toString());
      throw new DomainException("Invalid ticket");
    }
  }
}
