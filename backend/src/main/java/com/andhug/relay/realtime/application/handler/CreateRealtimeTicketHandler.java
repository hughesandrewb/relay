package com.andhug.relay.realtime.application.handler;

import com.andhug.relay.realtime.application.command.CreateRealtimeTicketCommand;
import com.andhug.relay.realtime.application.mapper.RealtimeTicketMapper;
import com.andhug.relay.realtime.domain.model.RealtimeTicket;
import com.andhug.relay.realtime.domain.repository.RealtimeTicketRepository;
import com.andhug.relay.realtime.infrastructure.web.dto.RealtimeTicketDto;
import com.andhug.relay.shared.application.handler.SyncCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRealtimeTicketHandler
    implements SyncCommandHandler<CreateRealtimeTicketCommand, RealtimeTicketDto> {

  private final RealtimeTicketRepository realtimeTicketRepository;

  private final RealtimeTicketMapper realtimeTicketMapper;

  public RealtimeTicketDto handle(CreateRealtimeTicketCommand command) {
    var ticket = RealtimeTicket.of(command.profileId(), command.ttlDuration());

    realtimeTicketRepository.save(ticket);

    return realtimeTicketMapper.toDto(ticket);
  }
}
