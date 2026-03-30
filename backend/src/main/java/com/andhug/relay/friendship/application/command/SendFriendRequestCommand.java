package com.andhug.relay.friendship.application.command;

import com.andhug.relay.shared.application.command.AsyncCommand;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;

public record SendFriendRequestCommand(ProfileId requesterId, ProfileId addresseeId)
    implements AsyncCommand {
  public SendFriendRequestCommand {
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

  public static SendFriendRequestCommand of(ProfileId requesterId, ProfileId addresseeId) {
    return new SendFriendRequestCommand(requesterId, addresseeId);
  }
}
