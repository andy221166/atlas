package org.atlas.edge.api.gateway.springcloudgateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthRulesProperties authRules;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults()))
        .authorizeExchange(auth -> {
          // Non-secured paths
          authRules.getNonSecuredPaths().forEach(path ->
              auth.pathMatchers(path).permitAll()
          );
          // Secured paths with role-based authorization
          authRules.getSecuredPaths().forEach(rule ->
              auth.pathMatchers(rule.getPath())
                  .hasAnyAuthority(rule.getRoles().toArray(String[]::new))
          );
          // Deny all other requests
          auth.anyExchange().denyAll();
        });

    return http.build();
  }
}
