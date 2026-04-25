package com.andhug.relay.presence.domain.model;

import com.andhug.relay.shared.domain.model.DeviceType;
import com.andhug.relay.shared.domain.model.GatewayId;
import com.andhug.relay.shared.domain.model.PresenceStatus;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionPresence {

  private ProfileId profileId;

  private GatewayId gatewayId;

  private PresenceStatus status;

  private DeviceType deviceType;

  private LocalDateTime connectedAt;

  public static SessionPresence of(
      ProfileId profileId, GatewayId gatewayId, DeviceType deviceType) {
    SessionPresence presence = new SessionPresence();
    presence.profileId = profileId;
    presence.gatewayId = gatewayId;
    presence.deviceType = deviceType;
    presence.status = PresenceStatus.ONLINE;
    presence.connectedAt = LocalDateTime.now();
    return presence;
  }
}
