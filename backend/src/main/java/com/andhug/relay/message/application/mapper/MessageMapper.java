package com.andhug.relay.message.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.andhug.relay.message.domain.model.Message;
import com.andhug.relay.message.infrastructure.persistence.MessageEntity;
import com.andhug.relay.message.infrastructure.web.dto.MessageDto;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.shared.application.mapper.ValueObjectMapper;

@Mapper(componentModel = "spring", uses = { ValueObjectMapper.class })
public interface MessageMapper {
    @Mapping(target = "id", source = "message.id")
    MessageDto toDto(Message message, Profile author);
    Message toDomain(MessageEntity entity);
    MessageEntity toEntity(Message message);
}
