package com.andhug.relay.config;

import com.andhug.relay.profile.Profile;
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
    RedisTemplate<String, Profile> profileRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Profile> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        JacksonJsonRedisSerializer<Profile> jacksonJsonRedisSerializer = new JacksonJsonRedisSerializer<>(Profile.class);
        template.setValueSerializer(jacksonJsonRedisSerializer);

        return template;
    }
}
