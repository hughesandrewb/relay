package com.andhug.relay.friendship.application.handler;

import com.andhug.relay.friendship.application.command.AcceptFriendRequestCommand;
import com.andhug.relay.friendship.domain.exception.NoPendingFriendRequestException;
import com.andhug.relay.friendship.domain.model.Friendship;
import com.andhug.relay.friendship.domain.model.FriendshipStatus;
import com.andhug.relay.friendship.domain.repository.FriendshipRepository;
import com.andhug.relay.shared.application.handler.AsyncCommandValidator;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AcceptFriendRequestValidator
    implements AsyncCommandValidator<AcceptFriendRequestCommand> {

  private final FriendshipRepository friendshipRepository;

  @Override
  @Transactional(readOnly = true)
  public void validate(AcceptFriendRequestCommand command) {

    ProfileId requesterId = command.requesterId();
    ProfileId addresseeId = command.addresseeId();

    Friendship friendship =
        friendshipRepository
            .findBetweenUsers(requesterId, addresseeId)
            .orElseThrow(() -> new NoPendingFriendRequestException(requesterId, addresseeId));

    if (friendship.getStatus() != FriendshipStatus.PENDING) {
      throw new NoPendingFriendRequestException(requesterId, addresseeId);
    }
  }
}
