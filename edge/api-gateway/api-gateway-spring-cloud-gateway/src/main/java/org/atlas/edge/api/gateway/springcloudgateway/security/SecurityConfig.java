package org.atlas.edge.api.gateway.springcloudgateway.security;

import lombok.RequiredArgsConstructor;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthRulesProperties authRules;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(Customizer.withDefaults())
        .oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwt ->
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        )
        .authorizeExchange(auth -> {
          // Permit preflight requests
          auth.pathMatchers(HttpMethod.OPTIONS).permitAll();

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

  @Bean
  public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
    ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      String role = jwt.getClaim(CustomClaim.USER_ROLE.getClaim());
      if (role != null) {
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        return Flux.just(authority);
      }
      return Flux.empty();
    });
    return converter;
  }
}
