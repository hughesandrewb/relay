package com.andhug.relay.message.internal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByRoomId(UUID roomId, Pageable pageable);
}
