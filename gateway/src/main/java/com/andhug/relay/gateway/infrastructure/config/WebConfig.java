package com.andhug.relay.gateway.infrastructure.config;

import com.andhug.relay.gateway.application.SessionRegistry;
import com.andhug.relay.gateway.infrastructure.ws.AttributeForwardingWebSocketService;
import com.andhug.relay.gateway.infrastructure.ws.GatewayWebSocketHandler;
import com.andhug.relay.realtime.domain.repository.PresenceRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@RequiredArgsConstructor
class WebConfig {

  private final SessionRegistry sessionRegistry;

  private final JsonMapper jsonMapper;

  private final GatewayProperties gatewayProperties;

  private final PresenceRepository presenceRepository;

  @Bean
  public HandlerMapping handlerMapping() {
    Map<String, WebSocketHandler> urlMap = new HashMap<>();
    urlMap.put(
        "/ws",
        new GatewayWebSocketHandler(
            gatewayProperties, sessionRegistry, presenceRepository, jsonMapper));

    return new SimpleUrlHandlerMapping(urlMap, SimpleUrlHandlerMapping.HIGHEST_PRECEDENCE);
  }

  @Bean
  public WebSocketHandlerAdapter webSocketHandlerAdapter() {
    return new WebSocketHandlerAdapter(new AttributeForwardingWebSocketService());
  }
}
