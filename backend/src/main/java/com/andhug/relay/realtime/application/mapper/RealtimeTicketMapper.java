package com.andhug.relay.realtime.application.mapper;

import org.mapstruct.Mapper;

import com.andhug.relay.realtime.domain.model.RealtimeTicket;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.realtime.infrastructure.web.dto.RealtimeTicketDto;

@Mapper(componentModel = "spring")
public interface RealtimeTicketMapper {
    RealtimeTicketDto toDto(RealtimeTicket domain);

    default String fromRealtimeTicketCode(RealtimeTicketCode code) {
        return code.value();
    }
}
