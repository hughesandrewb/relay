package com.andhug.relay.room.internal;

import com.andhug.relay.room.Room;
import com.andhug.relay.room.RoomDto;
import com.andhug.relay.workspace.api.Workspace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    
    Room toDomain(RoomDto dto);

    Room toDomain(RoomEntity dto);

    RoomDto toDto(Workspace domain);

    RoomEntity toEntity(RoomDto dto);

    RoomEntity toEntity(Workspace domain);
}