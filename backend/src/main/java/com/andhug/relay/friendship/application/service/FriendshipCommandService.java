package com.andhug.relay.friendship.application.service;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andhug.relay.friendship.application.command.AcceptFriendRequestCommand;
import com.andhug.relay.friendship.application.command.SendFriendRequestCommand;
import com.andhug.relay.friendship.domain.exception.AlreadyFriendsException;
import com.andhug.relay.friendship.domain.exception.AlreadySentFriendRequestException;
import com.andhug.relay.friendship.domain.exception.NoPendingFriendRequestException;
import com.andhug.relay.friendship.domain.exception.PreviouslyRejectedException;
import com.andhug.relay.friendship.domain.model.Friendship;
import com.andhug.relay.friendship.domain.model.FriendshipStatus;
import com.andhug.relay.friendship.domain.repository.FriendshipRepository;
import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.ProfileId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendshipCommandService {

    private final FriendshipRepository friendshipRepository;

    private final ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public void sendFriendRequest(SendFriendRequestCommand command) {

        ProfileId requesterId = command.requesterId();
        ProfileId addresseeId = command.addresseeId();

        Optional<Friendship> existingFriendship = friendshipRepository
            .findBetweenUsers(requesterId, addresseeId);

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

        var friendship = Friendship.builder()
            .requesterId(command.requesterId())
            .addresseeId(command.addresseeId())
            .status(FriendshipStatus.PENDING)
            .build();

        friendshipRepository.save(friendship);

        friendship
            .pullDomainEvents()
            .forEach(eventPublisher::publishEvent);
    }

    @Transactional
    public void acceptFriendRequest(AcceptFriendRequestCommand command) {

        ProfileId requesterId = command.requesterId();
        ProfileId addresseeId = command.addresseeId();

        Friendship friendship = friendshipRepository
            .findBetweenUsers(requesterId, addresseeId)
            .orElseThrow(() -> new NoPendingFriendRequestException(requesterId, addresseeId));
        friendship.accept();
        friendshipRepository.save(friendship);

        friendship
            .pullDomainEvents()
            .forEach(eventPublisher::publishEvent);
    }
}
