package com.andhug.relay.invite.infrastructure.web.dto;

import com.andhug.relay.profile.infrastructure.web.dto.ProfileDto;
import com.andhug.relay.workspace.infrastructure.web.dto.WorkspaceDto;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record InviteDto(
    String code,
    WorkspaceDto workspace,
    ProfileDto sender,
    LocalDateTime expiresAt,
    LocalDateTime createdAt) {}
