package com.andhug.relay.realtime.ws;

import com.andhug.relay.realtime.RealtimeTicketService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final RealtimeTicketService realtimeTicketService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        Optional<UUID> profileId = extractTicket(request)
                .flatMap(realtimeTicketService::validateRealtimeTicket);

        if (profileId.isEmpty()) {
            return false;
        }

        attributes.put("id", profileId.get());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }

    private Optional<String> extractTicket(ServerHttpRequest request) {

        if (!(request instanceof ServletServerHttpRequest)) {
            return Optional.empty();
        }

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        String ticket = servletRequest.getServletRequest().getParameter("ticket");

        return Optional.ofNullable(ticket)
                .filter(t -> !t.trim().isEmpty());
    }
}
