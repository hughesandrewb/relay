package com.andhug.relay.friendship.domain.exception;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.ProfileId;

public class NoPendingFriendRequestException extends DomainException {
  public NoPendingFriendRequestException(ProfileId requesterId, ProfileId addresseeId) {
    super(
        String.format(
            "No pending friend request between %s and %s",
            requesterId.toString(), addresseeId.toString()));
  }
}
