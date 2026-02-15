package com.andhug.relay.realtime.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ConnectionRegistry {

    private final Map<UUID, Connection> registry = new ConcurrentHashMap<>();

    public void registerConnection(Connection connection, UUID id) {

        this.registry.put(id, connection);

        log.info("Registered connection for {}", id);
    }

    public void unregisterConnection(UUID id) {

        this.registry.remove(id);

        log.info("Unregistered connection for {}", id);
    }

    public Connection getConnection(UUID id) {

        return this.registry.get(id);
    }

    public boolean isConnected(String id) {

        return this.registry.get(id).isActive();
    }
}
