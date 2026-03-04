package com.andhug.relay.room.api.dto;

import com.andhug.relay.profile.ProfileDto;
import com.andhug.relay.room.api.RoomType;
import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RoomDto(
        UUID id,
        String name,
        UUID workspaceId,
        Integer type,
        List<ProfileDto> participants
) {}
