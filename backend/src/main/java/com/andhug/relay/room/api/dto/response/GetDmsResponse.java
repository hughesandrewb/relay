package com.andhug.relay.room.api.dto.response;

import com.andhug.relay.room.api.dto.RoomDto;

import java.util.Set;

public record GetDmsResponse(
        Set<RoomDto> dms
) {}
