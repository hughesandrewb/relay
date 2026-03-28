package com.andhug.relay.message.application.mapper;

import org.mapstruct.Mapper;

import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.infrastructure.persistence.MessageEntity;
import com.andhug.relay.message.infrastructure.web.dto.MessageDto;
import com.andhug.relay.shared.application.mapper.ValueObjectMapper;

@Mapper(componentModel = "spring", uses = { ValueObjectMapper.class })
public interface MessageMapper {
    MessageDto toDto(Message message);
    Message toDomain(MessageEntity entity);
    MessageEntity toEntity(Message message);
}
