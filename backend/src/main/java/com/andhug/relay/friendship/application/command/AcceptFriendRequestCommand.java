package com.andhug.relay.friendship.application.command;

import com.andhug.relay.shared.application.command.AsyncCommand;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;

public record AcceptFriendRequestCommand(ProfileId requesterId, ProfileId addresseeId)
    implements AsyncCommand {

  public AcceptFriendRequestCommand {
    if (requesterId == null) {
      throw new InvalidArgumentException("RequesterId cannot be null");
    }
    if (addresseeId == null) {
      throw new InvalidArgumentException("AddresseeId cannot be null");
    }
    if (requesterId.equals(addresseeId)) {
      throw new InvalidArgumentException("Requester and addressee cannot be the same");
    }
  }

  public static AcceptFriendRequestCommand of(ProfileId requesterId, ProfileId addresseeId) {
    return new AcceptFriendRequestCommand(requesterId, addresseeId);
  }
}
