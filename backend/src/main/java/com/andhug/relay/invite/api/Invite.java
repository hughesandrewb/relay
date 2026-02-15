package com.andhug.relay.invite.api;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invite {

    private UUID id;

    private String code;

    private UUID workspaceId;

    private UUID senderId;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    public boolean isExpired() {

        return expiresAt.isBefore(LocalDateTime.now());
    }
}
