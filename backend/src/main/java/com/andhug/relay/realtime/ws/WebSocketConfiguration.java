package com.andhug.relay.realtime.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    WebSocketConnectionHandler webSocketConnectionHandler;

    UserHandshakeInterceptor userHandshakeInterceptor;

    public WebSocketConfiguration(WebSocketConnectionHandler webSocketConnectionHandler, UserHandshakeInterceptor userHandshakeInterceptor) {

        this.webSocketConnectionHandler = webSocketConnectionHandler;
        this.userHandshakeInterceptor = userHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry
                .addHandler(this.webSocketConnectionHandler, "/ws")
                .addInterceptors(this.userHandshakeInterceptor);
    }
}
