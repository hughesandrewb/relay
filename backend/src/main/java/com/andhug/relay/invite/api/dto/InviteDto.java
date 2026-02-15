package com.andhug.relay.invite.api.dto;

import com.andhug.relay.profile.ProfileDto;
import com.andhug.relay.workspace.api.dto.WorkspaceDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InviteDto(
        String code,
        WorkspaceDto workspace,
        ProfileDto sender,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {}
