package com.andhug.relay.room.internal;

import com.andhug.relay.room.api.Room;
import com.andhug.relay.room.api.dto.RoomDto;
import com.andhug.relay.workspace.api.Workspace;
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