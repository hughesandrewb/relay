package com.andhug.relay.friend.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FriendshipKey implements Serializable {

    @Column(name = "requester_id")
    private UUID requesterId;

    @Column(name = "addressee_id")
    private UUID addresseeId;
}
