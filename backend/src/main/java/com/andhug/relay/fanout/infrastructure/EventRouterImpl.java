package com.andhug.relay.fanout.infrastructure;

import com.andhug.relay.fanout.application.EventRouter;
import com.andhug.relay.shared.application.utils.JsonUtils;
import com.andhug.relay.shared.domain.model.GatewayId;
import com.andhug.relay.shared.domain.model.InboundEvent;
import com.andhug.relay.shared.domain.model.OutboundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRouterImpl implements EventRouter {

  /** Inbound channel name, relative to application i.e. gateway -> application services */
  private static final String INBOUND_CHANNEL_FORMAT = "gateway:%s:outbound";

  /** Outbound channel name, relative to application i.e. application services -> gateway */
  private static final String OUTBOUND_CHANNEL_FORMAT = "gateway:%s:inbound";

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public void forward(GatewayId gatewayId, OutboundEvent outboundEvent) {
    log.debug("Sending {} to gateway {}", JsonUtils.toJson(outboundEvent), gatewayId.toString());
    redisTemplate.convertAndSend(getOutboundChannel(gatewayId), JsonUtils.toJson(outboundEvent));
  }

  @Override
  public void receive(InboundEvent inboundEvent) {
    // TODO: Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'receive'");
  }

  private String getOutboundChannel(GatewayId gatewayId) {
    return OUTBOUND_CHANNEL_FORMAT.formatted(gatewayId.toString());
  }

  private String getInboundChannel(GatewayId gatewayId) {
    return INBOUND_CHANNEL_FORMAT.formatted(gatewayId.toString());
  }
}
