package com.andhug.relay.realtime.infrastructure.ws;

import com.andhug.relay.realtime.application.command.ValidateRealtimeTicketCommand;
import com.andhug.relay.realtime.application.handler.ValidateRealtimeTicketHandler;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

  private final ValidateRealtimeTicketHandler validateRealtimeTicketHandler;

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes)
      throws Exception {

    RealtimeTicketCode ticketCode;
    try {
      ticketCode = extractTicket(request);
    } catch (IllegalArgumentException e) {
      return false;
    }
    var validateRealtimeTicketCommand = ValidateRealtimeTicketCommand.of(ticketCode);
    ProfileId profileId = validateRealtimeTicketHandler.handle(validateRealtimeTicketCommand);

    attributes.put("id", profileId);

    return true;
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      @Nullable Exception exception) {}

  private RealtimeTicketCode extractTicket(ServerHttpRequest request) {

    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
    String ticket = servletRequest.getServletRequest().getParameter("ticket");

    try {
      return RealtimeTicketCode.of(ticket);
    } catch (InvalidArgumentException e) {
      throw new IllegalArgumentException("Invalid or missing ticket");
    }
  }
}
