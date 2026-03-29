package com.andhug.relay.realtime.application.mapper;

import com.andhug.relay.realtime.domain.model.RealtimeTicket;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.realtime.infrastructure.web.dto.RealtimeTicketDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RealtimeTicketMapper {
  RealtimeTicketDto toDto(RealtimeTicket domain);

  default String fromRealtimeTicketCode(RealtimeTicketCode code) {
    return code.value();
  }
}
