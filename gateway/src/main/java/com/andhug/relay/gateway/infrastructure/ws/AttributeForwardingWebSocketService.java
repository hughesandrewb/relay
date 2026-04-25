package com.andhug.relay.gateway.infrastructure.ws;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AttributeForwardingWebSocketService extends HandshakeWebSocketService {

  @Override
  public Mono<Void> handleRequest(ServerWebExchange exchange, WebSocketHandler webSocketHandler) {
    WebSocketHandler wrappedHandler =
        session -> {
          session.getAttributes().putAll(exchange.getAttributes());
          return webSocketHandler.handle(session);
        };
    return super.handleRequest(exchange, wrappedHandler);
  }
}
