package com.andhug.relay.friendship.domain.exception;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.ProfileId;

public class AlreadyFriendsException extends DomainException {

  public AlreadyFriendsException(ProfileId requesterId, ProfileId addresseeId) {
    super(
        String.format(
            "Requester %s and addressee %s are already friends",
            requesterId.toString(), addresseeId.toString()));
  }
}
