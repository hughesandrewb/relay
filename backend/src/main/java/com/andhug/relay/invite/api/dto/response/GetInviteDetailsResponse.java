package com.andhug.relay.invite.api.dto.response;

import com.andhug.relay.profile.ProfileDto;
import com.andhug.relay.workspace.api.dto.WorkspaceDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetInviteDetailsResponse(
        WorkspaceDto workspace,
        ProfileDto sender,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {}
