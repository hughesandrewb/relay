package com.andhug.relay.friend.api.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {

    private UUID requesterId;

    private UUID addresseeId;

    private FriendshipStatus status;
}
