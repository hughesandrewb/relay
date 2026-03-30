package com.andhug.relay.realtime.application.command;

import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;

public record ValidateRealtimeTicketCommand(RealtimeTicketCode ticketCode)
    implements SyncCommand<ProfileId> {

  public ValidateRealtimeTicketCommand {
    if (ticketCode == null) {
      throw new InvalidArgumentException("Ticket code cannot be null");
    }
  }

  public static ValidateRealtimeTicketCommand of(RealtimeTicketCode ticketCode) {
    return new ValidateRealtimeTicketCommand(ticketCode);
  }
}
