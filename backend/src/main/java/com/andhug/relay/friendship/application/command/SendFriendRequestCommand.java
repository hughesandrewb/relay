package com.andhug.relay.friendship.application.command;

import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.Builder;

@Builder
public record SendFriendRequestCommand(ProfileId requesterId, ProfileId addresseeId) {}
