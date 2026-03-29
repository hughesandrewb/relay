package com.andhug.relay.realtime.application.command;

import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;

public record ValidateRealtimeTicketCommand(RealtimeTicketCode ticketCode) {

  public ValidateRealtimeTicketCommand {
    if (ticketCode == null) {
      throw new InvalidArgumentException("Ticket code cannot be null");
    }
  }

  public static ValidateRealtimeTicketCommand of(RealtimeTicketCode ticketCode) {
    return new ValidateRealtimeTicketCommand(ticketCode);
  }
}
