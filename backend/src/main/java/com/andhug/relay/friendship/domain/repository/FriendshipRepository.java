package com.andhug.relay.friendship.domain.repository;

import com.andhug.relay.friendship.domain.model.Friendship;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Optional;

public interface FriendshipRepository {
  Optional<Friendship> findBetweenUsers(ProfileId profileId, ProfileId otherId);

  void save(Friendship friendship);
}
