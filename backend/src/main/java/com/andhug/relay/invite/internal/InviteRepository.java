package com.andhug.relay.invite.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<InviteEntity, UUID> {

    Optional<InviteEntity> findByWorkspaceIdAndSenderId(UUID workspaceId, UUID senderId);

    Optional<InviteEntity> findByCode(String code);

    Boolean existsByCode(String code);
}
