package com.andhug.relay.room.application.mapper;

import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.infrastructure.persistence.RoomEntity;
import com.andhug.relay.room.infrastructure.web.dto.RoomDto;
import com.andhug.relay.shared.application.mapper.ValueObjectMapper;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {ValueObjectMapper.class})
public interface RoomMapper {
  Room toDomain(RoomDto dto);

  Room toDomain(RoomEntity dto);

  RoomDto toDto(Room domain);

  RoomEntity toEntity(Room domain);
}
