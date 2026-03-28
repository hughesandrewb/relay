package com.andhug.relay.friendship.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.friendship.infrastructure.web.dto.FriendDto;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendshipQueryService {

    private final EntityManager entityManager;
    
    @Transactional(readOnly = true)
    public List<FriendDto> getFriends(UUID profileId) {
        String query = """
            SELECT new com.andhug.relay.friendship.infrastructure.web.dto.FriendDto(
                p.id, p.username, p.displayName, p.accentColor
            )
            FROM ProfileEntity p
            JOIN FriendshipEntity f
                ON (f.requesterId = :profileId AND f.addresseeId = p.id)
                OR (f.addresseeId = :profileId AND f.requesterId = p.id)
            WHERE f.status = 'ACCEPTED'
        """;

        return entityManager.createQuery(query, FriendDto.class)
                .setParameter("profileId", profileId)
                .getResultList();
    }
}
