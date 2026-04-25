package com.andhug.relay.fanout.application;

import com.andhug.relay.shared.domain.model.GatewayId;
import com.andhug.relay.shared.domain.model.InboundEvent;
import com.andhug.relay.shared.domain.model.OutboundEvent;

public interface EventRouter {
  void forward(GatewayId gatewayId, OutboundEvent outboundEvent);

  void receive(InboundEvent inboundEvent);
}
