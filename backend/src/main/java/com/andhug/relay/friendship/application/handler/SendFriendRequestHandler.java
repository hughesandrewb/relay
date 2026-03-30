package com.andhug.relay.friendship.application.handler;

import com.andhug.relay.friendship.application.command.SendFriendRequestCommand;
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
public class SendFriendRequestHandler implements AsyncCommandHandler<SendFriendRequestCommand> {

  private final FriendshipRepository friendshipRepository;

  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public void handle(SendFriendRequestCommand command) {

    ProfileId requesterId = command.requesterId();
    ProfileId addresseeId = command.addresseeId();

    var friendship = Friendship.create(requesterId, addresseeId);

    friendshipRepository.save(friendship);

    friendship.pullDomainEvents().forEach(eventPublisher::publishEvent);
  }
}
