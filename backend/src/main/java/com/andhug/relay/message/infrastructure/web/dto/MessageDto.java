package com.andhug.relay.message.infrastructure.web.dto;

import lombok.Builder;

import java.util.UUID;

import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;

@Builder
public record MessageDto(
        Long id,
        ProfileDto author,
        UUID roomId,
        String content
) {}
