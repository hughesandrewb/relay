package com.andhug.relay.message.infrastructure.web.dto;

import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageDto(Long id, ProfileDto author, UUID roomId, String content) {}
