package com.andhug.relay.friendship.domain.exception;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.ProfileId;

public class AlreadySentFriendRequestException extends DomainException {
	public AlreadySentFriendRequestException(ProfileId requesterId, ProfileId addresseeId) {
		super(String.format("Friendship request already exists between %s and %s", requesterId, addresseeId));
	}
}
