package org.atlas.edge.api.gateway.springcloudgateway.security;

import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderRelayFilter extends
    AbstractGatewayFilterFactory<JwtHeaderRelayFilter.Config> {

  public JwtHeaderRelayFilter() {
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
          // Extract claims with null checks
          String userId = jwt.getClaimAsString(CustomClaim.USER_ID.getClaim());
          String userRole = jwt.getClaimAsString(CustomClaim.USER_ROLE.getClaim());

          // Mutate the request to add custom headers
          exchange.getRequest().mutate()
              .header(CustomClaim.USER_ID.getHeader(), userId)
              .header(CustomClaim.USER_ROLE.getHeader(), userRole)
              .build();

          return chain.filter(exchange);
        });
  }

  public static class Config {
    // Configuration properties if needed
  }
}