package com.andhug.relay.message.api.dto;

import com.andhug.relay.profile.ProfileDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MessageDto(
        long id,
        ProfileDto author,
        UUID roomId,
        String content
) {}
