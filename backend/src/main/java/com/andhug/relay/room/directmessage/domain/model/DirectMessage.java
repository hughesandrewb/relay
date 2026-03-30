package com.andhug.relay.room.directmessage.domain.model;

import com.andhug.relay.shared.domain.model.AggregateRoot;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.RoomId;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectMessage extends AggregateRoot {

  private RoomId id;

  private Set<ProfileId> participants;

  public static DirectMessage create(Set<ProfileId> participants) {
    return DirectMessage.builder().id(RoomId.generate()).participants(participants).build();
  }

  public boolean isGroupDm() {
    return participants.size() > 2;
  }
}
