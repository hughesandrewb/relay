package com.andhug.relay.shared.domain.model;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePresence {

  private PresenceStatus status;

  private CustomStatus customStatus;

  private Set<GatewayId> gatewayIds;

  public static ProfilePresence of(PresenceStatus status, CustomStatus customStatus) {
    return new ProfilePresence(status, customStatus, new HashSet<>());
  }

  public void updateStatus(PresenceStatus newStatus) {
    this.status = newStatus;
  }

  public void addGateway(GatewayId gatewayId) {
    if (this.gatewayIds == null) {
      this.gatewayIds = new HashSet<>();
    }

    this.gatewayIds.add(gatewayId);
  }
}
