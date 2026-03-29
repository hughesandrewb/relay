package com.andhug.relay.message.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {
  List<MessageEntity> findByRoomId(UUID roomId);
}
