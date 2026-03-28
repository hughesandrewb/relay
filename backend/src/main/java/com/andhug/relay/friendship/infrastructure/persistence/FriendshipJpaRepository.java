package com.andhug.relay.friendship.infrastructure.persistence;

import com.andhug.relay.friendship.domain.model.FriendshipStatus;
import com.andhug.relay.profile.infrastructure.persistence.ProfileEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipJpaRepository extends JpaRepository<FriendshipEntity, UUID> {

    @Query("""
        SELECT f FROM FriendshipEntity f
        WHERE (f.requesterId = :profileId AND f.addresseeId = :otherId)
            OR (f.requesterId = :otherId AND f.addresseeId = :profileId)
    """)
    Optional<FriendshipEntity> findBetweenUsers(@Param("profileId") UUID profileId, @Param("otherId") UUID otherId);

    @Query("""
        SELECT f.addresseeId FROM FriendshipEntity f
        WHERE f.requesterId = :profileId AND f.status = 'ACCEPTED'
        UNION
        SELECT f.requesterId FROM FriendshipEntity f
        WHERE f.addresseeId = :profileId AND f.status = 'ACCEPTED'
    """)
    List<ProfileEntity> findAcceptedFriends(@Param("profileId") UUID profileId);

    // requests received by a user
    List<FriendshipEntity> findByAddresseeIdAndStatus(UUID addresseeId, FriendshipStatus status);

    // requests sent by a user
    List<FriendshipEntity> findByRequesterIdAndStatus(UUID requesterId, FriendshipStatus status);

    Optional<FriendshipEntity> findByRequesterIdAndAddresseeId(UUID requesterId, UUID AddresseeId);
}
