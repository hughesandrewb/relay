package com.andhug.relay.gateway.infrastructure.ws;

import com.andhug.relay.gateway.application.service.RealtimeTicketService;
import com.andhug.relay.realtime.domain.model.RealtimeTicketCode;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GatewayWebSocketAuthFilter implements WebFilter {

  private final RealtimeTicketService realtimeTicketService;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    if ("/ws".equals(exchange.getRequest().getURI().getPath())) {
      RealtimeTicketCode ticketCode;
      try {
        ticketCode = extractTicket(exchange);
      } catch (IllegalArgumentException e) {
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }

      ProfileId profileId = realtimeTicketService.validateTicket(ticketCode);
      exchange.getAttributes().put("profileId", profileId);

      return chain.filter(exchange);
    } else {
      exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
      return exchange.getResponse().setComplete();
    }
  }

  private RealtimeTicketCode extractTicket(ServerWebExchange exchange) {
    String ticket = exchange.getRequest().getQueryParams().getFirst("ticket");

    try {
      return RealtimeTicketCode.of(ticket);
    } catch (InvalidArgumentException e) {
      throw new IllegalArgumentException("Invalid or missing ticket");
    }
  }
}
