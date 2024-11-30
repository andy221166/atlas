package org.atlas.edge.gateway.ratelimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(RedisConfigProperties.class)
@RequiredArgsConstructor
public class RedisConfig {

  private final RedisConfigProperties properties;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(properties.getHost(),
        properties.getPort());
    config.setPassword(properties.getPassword());
    return new LettuceConnectionFactory(config);
  }

  @Bean
  public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
      LettuceConnectionFactory redisConnectionFactory) {
    RedisSerializationContext<String, String> serializationContext = RedisSerializationContext
        .<String, String>newSerializationContext(new StringRedisSerializer())
        .build();
    return new ReactiveRedisTemplate<>(redisConnectionFactory, serializationContext);
  }
}
