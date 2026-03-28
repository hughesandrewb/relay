package com.andhug.relay.room.directmessage.infrastructure.web.dto;

import java.util.List;
import java.util.UUID;

import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;

public record DirectMessageDto(
    UUID id,
    List<ProfileDto> participants
) {}
