package com.andhug.relay.room.directmessage.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andhug.relay.room.directmessage.domain.model.DirectMessage;
import com.andhug.relay.room.directmessage.infrastructure.persistence.DirectMessageEntity;
import com.andhug.relay.room.directmessage.infrastructure.web.dto.DirectMessageDto;
import com.andhug.relay.shared.application.mapper.ValueObjectMapper;

@Mapper(componentModel = "spring", uses = { ValueObjectMapper.class })
public interface DirectMessageMapper {
    DirectMessage toDomain(DirectMessageEntity entity);
    @Mapping(target = "participants", ignore = true)
    DirectMessageDto toDto(DirectMessage domain);
    DirectMessageEntity toEntity(DirectMessage domain);
}
