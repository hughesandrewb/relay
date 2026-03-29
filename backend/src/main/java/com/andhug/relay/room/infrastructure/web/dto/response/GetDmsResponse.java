package com.andhug.relay.room.infrastructure.web.dto.response;

import com.andhug.relay.room.infrastructure.web.dto.RoomDto;
import java.util.Set;

public record GetDmsResponse(Set<RoomDto> dms) {}
