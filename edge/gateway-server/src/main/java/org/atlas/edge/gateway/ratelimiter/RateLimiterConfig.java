package org.atlas.edge.gateway.ratelimiter;

import org.atlas.commons.constant.CustomHeaders;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Optional.ofNullable(exchange.getRequest()
                .getHeaders()
                .getFirst(CustomHeaders.USER_ID))
            .map(Mono::just)
            .orElseGet(Mono::empty);
    }
}
