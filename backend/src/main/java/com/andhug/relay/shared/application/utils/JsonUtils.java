package com.andhug.relay.shared.application.utils;

import tools.jackson.databind.ObjectMapper;

public class JsonUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String toJson(Object object) {
    return mapper.writeValueAsString(object);
  }
}
