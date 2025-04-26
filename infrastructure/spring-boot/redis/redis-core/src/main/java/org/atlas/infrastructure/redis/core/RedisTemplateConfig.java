package org.atlas.infrastructure.redis.core;

import org.atlas.framework.json.jackson.JacksonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

  @Bean
  public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(connectionFactory);

    // Key serialization
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);

    // Value serialization
    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(
        JacksonService.objectMapper);
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashValueSerializer(serializer);

    return redisTemplate;
  }
}
