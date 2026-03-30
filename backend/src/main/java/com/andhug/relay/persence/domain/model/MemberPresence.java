package com.andhug.relay.persence.domain.model;

import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.Getter;

@Getter
public class MemberPresence {

  private ProfileId profileId;

  private PresenceStatus status;

  public void updateStatus(PresenceStatus newStatus) {
    this.status = newStatus;
  }
}
