package com.andhug.relay.room.application.mapper;

import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.infrastructure.persistence.RoomEntity;
import com.andhug.relay.room.infrastructure.web.dto.RoomDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    
    Room toDomain(RoomDto dto);

    @Mapping(target = "participants", ignore = true)
    Room toDomain(RoomEntity dto);

    RoomDto toDto(Room domain);

    @Mapping(target = "participants", ignore = true)
    RoomEntity toEntity(RoomDto dto);

    @Mapping(target = "participants", ignore = true)
    RoomEntity toEntity(Room domain);
}
