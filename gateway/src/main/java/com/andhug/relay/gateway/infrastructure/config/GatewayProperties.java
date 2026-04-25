package com.andhug.relay.gateway.infrastructure.config;

import com.andhug.relay.shared.domain.model.GatewayId;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("gateway")
public class GatewayProperties {
  private final GatewayId gatewayId;

  public GatewayProperties(String gatewayId) {
    this.gatewayId = GatewayId.of(gatewayId);
  }
}
