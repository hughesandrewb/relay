package com.andhug.relay.shared.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.JsonNode;

/** Container for events from the client i.e. client -> gateway */
public record InboundEvent(@JsonProperty("op") Integer opcode, @JsonProperty("d") JsonNode data) {}
