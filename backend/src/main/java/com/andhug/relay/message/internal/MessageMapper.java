package com.andhug.relay.message.internal;

import com.andhug.relay.message.api.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Message toDomain(MessageEntity entity);

    MessageEntity toEntity(Message message);
}
