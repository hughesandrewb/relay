package com.andhug.relay.realtime.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    WebSocketConnectionHandler webSocketConnectionHandler;

    AuthHandshakeInterceptor authHandshakeInterceptor;

    public WebSocketConfiguration(WebSocketConnectionHandler webSocketConnectionHandler, AuthHandshakeInterceptor authHandshakeInterceptor) {

        this.webSocketConnectionHandler = webSocketConnectionHandler;
        this.authHandshakeInterceptor = authHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry
                .addHandler(this.webSocketConnectionHandler, "/ws")
                .addInterceptors(this.authHandshakeInterceptor)
                .setAllowedOrigins("http://localhost:5173");
    }
}
