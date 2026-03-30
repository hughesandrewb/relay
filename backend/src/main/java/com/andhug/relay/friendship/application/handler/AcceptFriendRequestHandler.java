package com.andhug.relay.friendship.application.handler;

import com.andhug.relay.friendship.application.command.AcceptFriendRequestCommand;
import com.andhug.relay.friendship.domain.exception.NoPendingFriendRequestException;
import com.andhug.relay.friendship.domain.model.Friendship;
import com.andhug.relay.friendship.domain.repository.FriendshipRepository;
import com.andhug.relay.shared.application.handler.AsyncCommandHandler;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AcceptFriendRequestHandler implements AsyncCommandHandler<AcceptFriendRequestCommand> {

  private final FriendshipRepository friendshipRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public void handle(AcceptFriendRequestCommand command) {

    ProfileId requesterId = command.requesterId();
    ProfileId addresseeId = command.addresseeId();

    Friendship friendship =
        friendshipRepository
            .findBetweenUsers(requesterId, addresseeId)
            .orElseThrow(() -> new NoPendingFriendRequestException(requesterId, addresseeId));
    friendship.accept();
    friendshipRepository.save(friendship);

    friendship.pullDomainEvents().forEach(eventPublisher::publishEvent);
  }
}
