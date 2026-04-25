package com.andhug.relay.gateway.application;

import com.andhug.relay.shared.domain.model.InboundEvent;
import com.andhug.relay.shared.domain.model.OutboundEvent;
import reactor.core.publisher.Mono;

public interface EventRouter {
  public Mono<Void> forward(InboundEvent inboundEvent);

  public void receive(OutboundEvent outboundEvent);
}
