package com.andhug.relay.friendship.domain.model;

import com.andhug.relay.shared.domain.exception.DomainException;
import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.FriendshipId;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendship extends AggregateRoot {

  private FriendshipId id;

  private ProfileId requesterId;

  private ProfileId addresseeId;

  private FriendshipStatus status;

  private Instant createdAt;

  private Instant updatedAt;

  public static Friendship create(ProfileId requesterId, ProfileId addresseeId) {
    return Friendship.builder()
        .id(FriendshipId.generate())
        .requesterId(requesterId)
        .addresseeId(addresseeId)
        .status(FriendshipStatus.PENDING)
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  public void accept() {
    if (!this.isPending()) {
      throw new DomainException("Friendship not in PENDING status");
    }
    this.status = FriendshipStatus.ACCEPTED;
  }

  public void reject() {
    if (!this.isPending()) {
      throw new DomainException("Friendship not in PENDING status");
    }
    this.status = FriendshipStatus.REJECTED;
  }

  public boolean isPending() {
    return FriendshipStatus.PENDING.equals(this.status);
  }
}
