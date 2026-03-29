package com.andhug.relay.room.directmessage.infrastructure.web.dto;

import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import java.util.List;
import java.util.UUID;

public record DirectMessageDto(UUID id, List<ProfileDto> participants) {}
