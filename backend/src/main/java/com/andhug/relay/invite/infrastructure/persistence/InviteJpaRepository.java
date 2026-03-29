package com.andhug.relay.invite.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteJpaRepository extends JpaRepository<InviteEntity, UUID> {

  Optional<InviteEntity> findByWorkspaceIdAndSenderId(UUID workspaceId, UUID senderId);

  Optional<InviteEntity> findByCode(String code);

  Boolean existsByCode(String code);
}
