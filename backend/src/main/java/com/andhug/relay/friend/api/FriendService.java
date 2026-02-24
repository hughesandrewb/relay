package com.andhug.relay.friend.api;

import com.andhug.relay.friend.api.models.Friendship;
import com.andhug.relay.friend.api.models.FriendshipStatus;
import com.andhug.relay.friend.internal.FriendshipEntity;
import com.andhug.relay.friend.internal.FriendshipKey;
import com.andhug.relay.friend.internal.FriendshipMapper;
import com.andhug.relay.friend.internal.FriendshipRepository;
import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileMapper;
import com.andhug.relay.profile.internal.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendshipRepository friendshipRepository;

    private final ProfileRepository profileRepository;

    private final FriendshipMapper friendshipMapper;

    private final ProfileMapper profileMapper;

    @Transactional(readOnly = true)
    public List<Profile> getFriends(UUID profileId, Pageable pageable) {

        List<ProfileEntity> profileEntities = friendshipRepository.findAcceptedFriends(profileId);

        return profileEntities.stream()
                .map(profileMapper::toDomain)
                .toList();
    }

    @Transactional
    public Friendship sendRequest(UUID requesterId, UUID addresseeId) {

        if (requesterId.equals(addresseeId)) {
            throw new IllegalArgumentException("Cannot send a friend request to yourself");
        }

        friendshipRepository.findBetweenUsers(requesterId, addresseeId).ifPresent(f -> {
            if (f.getStatus().equals(FriendshipStatus.ACCEPTED)
                    || f.getStatus().equals(FriendshipStatus.REJECTED)) {
                throw new IllegalArgumentException("A friendship already exists");
            }
            if (f.getStatus().equals(FriendshipStatus.PENDING)) {
                throw new IllegalArgumentException("A friendship request already exists");
            }
        });

        ProfileEntity requester = profileRepository.getReferenceById(requesterId);
        ProfileEntity addressee = profileRepository.getReferenceById(addresseeId);

        FriendshipEntity friendshipEntity = friendshipRepository.save(
                FriendshipEntity.builder()
                        .id(FriendshipKey.builder()
                                .requesterId(requesterId)
                                .addresseeId(addresseeId)
                                .build())
                        .requester(requester)
                        .addressee(addressee)
                        .status(FriendshipStatus.PENDING)
                        .build());

        return friendshipMapper.toDomain(friendshipEntity);
    }

    @Transactional
    public Friendship acceptRequest(UUID requesterId, UUID addresseeId) {
        FriendshipEntity friendship = friendshipRepository
                .findByRequesterIdAndAddresseeId(requesterId, addresseeId)
                .orElseThrow(() -> new IllegalArgumentException("No pending friend request found"));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("Friend request is already " + friendship.getStatus().name().toLowerCase());
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);

        return friendshipMapper.toDomain(friendship);
    }
}
