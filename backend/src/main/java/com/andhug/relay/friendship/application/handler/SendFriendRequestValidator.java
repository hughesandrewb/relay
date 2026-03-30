package com.andhug.relay.friendship.application.handler;

import com.andhug.relay.friendship.application.command.SendFriendRequestCommand;
import com.andhug.relay.friendship.domain.exception.AlreadyFriendsException;
import com.andhug.relay.friendship.domain.exception.AlreadySentFriendRequestException;
import com.andhug.relay.friendship.domain.exception.PreviouslyRejectedException;
import com.andhug.relay.friendship.domain.model.Friendship;
import com.andhug.relay.friendship.domain.repository.FriendshipRepository;
import com.andhug.relay.shared.application.handler.AsyncCommandValidator;
import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SendFriendRequestValidator implements AsyncCommandValidator<SendFriendRequestCommand> {

  private final FriendshipRepository friendshipRepository;

  @Override
  @Transactional(readOnly = true)
  public void validate(SendFriendRequestCommand command) {

    ProfileId requesterId = command.requesterId();
    ProfileId addresseeId = command.addresseeId();

    Optional<Friendship> existingFriendship =
        friendshipRepository.findBetweenUsers(requesterId, addresseeId);

    if (existingFriendship.isPresent()) {
      switch (existingFriendship.get().getStatus()) {
        case ACCEPTED:
          throw new AlreadyFriendsException(requesterId, addresseeId);
        case REJECTED:
          throw new PreviouslyRejectedException(requesterId, addresseeId);
        case PENDING:
          throw new AlreadySentFriendRequestException(requesterId, addresseeId);
        default:
          throw new DomainException("Unknown FriendshipStatus found");
      }
    }
  }
}
