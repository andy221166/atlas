package org.atlas.edge.api.gateway.springcloudgateway.ratelimiter;

import org.apache.commons.lang3.StringUtils;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RoleBasedKeyResolver implements KeyResolver {

  private static final String ANONYMOUS_KEY = "anonymous";

  @Override
  public Mono<String> resolve(ServerWebExchange exchange) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(auth -> auth != null && auth.isAuthenticated() && auth.getCredentials() instanceof Jwt)
        .map(auth -> (Jwt) auth.getCredentials())
        .map(jwt -> {
          String userRole = jwt.getClaimAsString(CustomClaim.USER_ROLE.getClaim());
          String userId = jwt.getSubject(); // typically the user's ID or username
          if (StringUtils.isNotBlank(userRole) && StringUtils.isNotBlank(userId)) {
            return userRole.toLowerCase() + ":" + userId;
          }
          return ANONYMOUS_KEY;
        })
        .defaultIfEmpty(ANONYMOUS_KEY);
  }
}
