package com.andhug.relay.friendship.domain.exception;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.ProfileId;

public class PreviouslyRejectedException extends DomainException {
  public PreviouslyRejectedException(ProfileId requesterId, ProfileId addresseeId) {
    super(
        String.format(
            "Requester %s was previously rejected by %s",
            requesterId.toString(), addresseeId.toString()));
  }
}
