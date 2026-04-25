package com.andhug.relay.gateway.domain.exception;

import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.shared.domain.exception.DomainException;

public class InvalidRealtimeTicketException extends DomainException {
  public InvalidRealtimeTicketException(RealtimeTicketCode ticketCode) {
    super("Invalid realtime ticket: " + ticketCode.toString());
  }
}
