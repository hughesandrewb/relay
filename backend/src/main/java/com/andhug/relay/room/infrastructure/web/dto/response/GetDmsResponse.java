package com.andhug.relay.room.infrastructure.web.dto.response;

import java.util.Set;

import com.andhug.relay.room.infrastructure.web.dto.RoomDto;

public record GetDmsResponse(
        Set<RoomDto> dms
) {}
