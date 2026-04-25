package com.andhug.relay.gateway.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Event<T>(
    @JsonProperty("op") Opcode opcode,
    @JsonProperty("d") T data,
    @JsonProperty("s") Integer sequence,
    @JsonProperty("t") String type) {

  public static <T> Event<T> of(Opcode opcode, T data, Integer sequence, String type) {
    return new Event<>(opcode, data, sequence, type);
  }
}
