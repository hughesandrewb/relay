package com.andhug.relay.realtime.infrastructure.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

  private final WebSocketConnectionHandler webSocketConnectionHandler;

  private final AuthHandshakeInterceptor authHandshakeInterceptor;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
        .addHandler(this.webSocketConnectionHandler, "/ws")
        .addInterceptors(this.authHandshakeInterceptor)
        .setAllowedOrigins("http://localhost:5173");
  }
}
