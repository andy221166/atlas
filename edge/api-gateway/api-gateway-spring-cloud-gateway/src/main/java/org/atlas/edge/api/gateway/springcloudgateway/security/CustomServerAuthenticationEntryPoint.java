package org.atlas.edge.api.gateway.springcloudgateway.security;

import org.atlas.edge.api.gateway.springcloudgateway.util.SpringWebFluxUtil;
import org.atlas.framework.api.server.rest.response.Response;
import org.atlas.framework.error.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Used for Spring WebFlux
 */
@Component
public class CustomServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
    Response<Void> responseBody = Response.error(AppError.UNAUTHORIZED.getErrorCode(),
        ex.getMessage());
    return SpringWebFluxUtil.respond(exchange, responseBody, HttpStatus.UNAUTHORIZED);
  }
}
