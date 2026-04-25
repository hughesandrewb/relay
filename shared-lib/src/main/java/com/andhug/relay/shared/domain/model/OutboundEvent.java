package com.andhug.relay.shared.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;

/** Container for events destined for the client i.e. gateway -> client */
public record OutboundEvent(
    ProfileId profileId,
    @JsonProperty("t") String eventType,
    @JsonProperty("d") JsonNode eventData) {}
