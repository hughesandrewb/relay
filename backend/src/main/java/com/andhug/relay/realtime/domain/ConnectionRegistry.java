package com.andhug.relay.realtime.domain;

import com.andhug.relay.realtime.domain.model.Connection;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConnectionRegistry {

  private final Map<UUID, Connection> registry = new ConcurrentHashMap<>();

  public void registerConnection(Connection connection, ProfileId profileId) {

    this.registry.put(profileId.value(), connection);

    log.info("Registered connection for {}", profileId);
  }

  public void unregisterConnection(ProfileId profileId) {

    this.registry.remove(profileId.value());

    log.info("Unregistered connection for {}", profileId);
  }

  public Connection getConnection(ProfileId profileId) {

    return this.registry.get(profileId.value());
  }

  public boolean isConnected(ProfileId profileId) {

    return this.registry.get(profileId.value()).isActive();
  }
}
