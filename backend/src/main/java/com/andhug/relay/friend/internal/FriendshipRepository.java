package com.andhug.relay.friend.internal;

import com.andhug.relay.friend.api.models.FriendshipStatus;
import com.andhug.relay.profile.internal.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, FriendshipKey> {

    @Query("""
                SELECT f FROM FriendshipEntity f
                WHERE (f.requester.id = :profileId AND f.addressee.id = :otherId)
                   OR (f.requester.id = :otherId AND f.addressee.id = :profileId)
            """)
    Optional<FriendshipEntity> findBetweenUsers(@Param("profileId") UUID profileId, @Param("otherId") UUID otherId);

    @Query("""
                SELECT f.addressee FROM FriendshipEntity f
                WHERE f.requester.id = :profileId AND f.status = 'ACCEPTED'
                UNION
                SELECT f.requester FROM FriendshipEntity f
                WHERE f.addressee.id = :profileId AND f.status = 'ACCEPTED'
            """)
    List<ProfileEntity> findAcceptedFriends(@Param("profileId") UUID profileId);

    // requests received by a user
    List<FriendshipEntity> findByAddresseeIdAndStatus(UUID addresseeId, FriendshipStatus status);

    // requests sent by a user
    List<FriendshipEntity> findByRequesterIdAndStatus(UUID requesterId, FriendshipStatus status);
}
