package com.andhug.relay.gateway.infrastructure.config;

import com.andhug.relay.shared.domain.model.ProfilePresence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  LettuceConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory();
  }

  @Bean
  RedisTemplate<String, ProfilePresence> profilePresenceRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, ProfilePresence> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);

    template.setKeySerializer(new StringRedisSerializer());

    JacksonJsonRedisSerializer<ProfilePresence> jacksonJsonRedisSerializer =
        new JacksonJsonRedisSerializer<>(ProfilePresence.class);
    template.setValueSerializer(jacksonJsonRedisSerializer);

    return template;
  }
}
