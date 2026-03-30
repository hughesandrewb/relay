package com.andhug.relay.realtime.application.service;

import com.andhug.relay.realtime.domain.event.RealtimeConnectionClosedEvent;
import com.andhug.relay.realtime.domain.event.RealtimeConnectionOpenedEvent;
import com.andhug.relay.realtime.domain.model.Connection;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectionRegistry {

  private final Map<UUID, Connection> registry = new ConcurrentHashMap<>();

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void registerConnection(Connection connection, ProfileId profileId) {

    this.registry.put(profileId.value(), connection);

    eventPublisher.publishEvent(RealtimeConnectionOpenedEvent.of(profileId));

    log.info("Registered connection for {}", profileId);
  }

  public void unregisterConnection(ProfileId profileId) {

    this.registry.remove(profileId.value());

    eventPublisher.publishEvent(RealtimeConnectionClosedEvent.of(profileId));

    log.info("Unregistered connection for {}", profileId);
  }

  public Connection getConnection(ProfileId profileId) {

    return this.registry.get(profileId.value());
  }

  public boolean isConnected(ProfileId profileId) {

    return this.registry.get(profileId.value()).isActive();
  }
}
