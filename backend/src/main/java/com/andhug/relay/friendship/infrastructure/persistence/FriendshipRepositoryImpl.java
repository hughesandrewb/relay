package com.andhug.relay.friendship.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.andhug.relay.friendship.application.mapper.FriendshipMapper;
import com.andhug.relay.friendship.domain.model.Friendship;
import com.andhug.relay.friendship.domain.repository.FriendshipRepository;
import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {

    private final FriendshipJpaRepository friendshipJpaRepository;

    private final FriendshipMapper friendshipMapper;

	@Override
	public Optional<Friendship> findBetweenUsers(ProfileId profileId, ProfileId otherId) {
        return friendshipJpaRepository
            .findBetweenUsers(profileId.value(), otherId.value())
            .map(friendshipMapper::toDomain);
	}

	@Override
	public void save(Friendship friendship) {
        FriendshipEntity entity = friendshipMapper.toEntity(friendship);
        friendshipJpaRepository.save(entity);
	}
}
