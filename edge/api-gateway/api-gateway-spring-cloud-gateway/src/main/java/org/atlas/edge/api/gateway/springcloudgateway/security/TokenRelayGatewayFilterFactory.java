package org.atlas.edge.api.gateway.springcloudgateway.security;

import org.apache.http.HttpHeaders;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class TokenRelayGatewayFilterFactory extends
    AbstractGatewayFilterFactory<TokenRelayGatewayFilterFactory.Config> {

  public TokenRelayGatewayFilterFactory() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(auth ->
            auth != null && auth.isAuthenticated() && auth.getCredentials() instanceof Jwt)
        .map(auth -> (Jwt) auth.getCredentials())
        .flatMap(jwt -> {
          // Extract custom headers from JWT
          String userId = jwt.getSubject();
          String userRole = jwt.getClaimAsString(CustomClaim.USER_ROLE.getClaim());
          String sessionId = jwt.getClaimAsString(CustomClaim.SESSION_ID.getClaim());

          // Mutate the request to add custom headers
          ServerHttpRequest mutatedRequest = exchange.getRequest()
              .mutate()
              .headers(httpHeaders -> {
                httpHeaders.remove(HttpHeaders.AUTHORIZATION);
                httpHeaders.set(CustomClaim.SESSION_ID.getHeader(), sessionId);
                httpHeaders.set(CustomClaim.USER_ID.getHeader(), userId);
                httpHeaders.set(CustomClaim.USER_ROLE.getHeader(), userRole);
                assert jwt.getExpiresAt() != null;
                httpHeaders.set(CustomClaim.EXPIRES_AT.getHeader(),
                    String.valueOf(jwt.getExpiresAt().toEpochMilli()));
              })
              .build();
          ServerWebExchange mutatedExchange = exchange.mutate()
              .request(mutatedRequest)
              .build();
          return chain.filter(mutatedExchange);
        });
  }

  public static class Config {
    // Configuration properties if needed
  }
}
