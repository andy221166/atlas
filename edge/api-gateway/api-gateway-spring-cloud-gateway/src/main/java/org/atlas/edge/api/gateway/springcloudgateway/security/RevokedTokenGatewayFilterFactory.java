package org.atlas.edge.api.gateway.springcloudgateway.security;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.edge.api.gateway.springcloudgateway.util.SpringWebFluxUtil;
import org.atlas.framework.api.server.rest.response.Response;
import org.atlas.framework.constant.SecurityConstant;
import org.atlas.framework.error.AppError;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RevokedTokenGatewayFilterFactory extends
    AbstractGatewayFilterFactory<RevokedTokenGatewayFilterFactory.Config> {

  private final RedisTemplate<String, Object> redisTemplate;

  public RevokedTokenGatewayFilterFactory(RedisTemplate<String, Object> redisTemplate) {
    super(Config.class);
    this.redisTemplate = redisTemplate;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      List<String> authorizationHeaders = exchange.getRequest()
          .getHeaders()
          .get(HttpHeaders.AUTHORIZATION);
      if (CollectionUtils.isEmpty(authorizationHeaders)) {
        return chain.filter(exchange);
      }
      String accessToken = authorizationHeaders.get(0);
      final String tokenBlacklistRedisKey =
          SecurityConstant.TOKEN_BLACKLIST_REDIS_KEY_PREFIX + accessToken;

      // Check if the access token is in the Redis blacklist
      Boolean isBlacklisted = redisTemplate.hasKey(tokenBlacklistRedisKey);

      if (Boolean.TRUE.equals(isBlacklisted)) {
        Response<Void> responseBody =
            Response.error(AppError.UNAUTHORIZED.getErrorCode(), "Token has been revoked");
        return SpringWebFluxUtil.respond(exchange, responseBody, HttpStatus.UNAUTHORIZED);
      }

      // Proceed with the request if not blacklisted
      return chain.filter(exchange);
    };
  }

  public static class Config {
    // Add configuration properties if needed
  }
}
