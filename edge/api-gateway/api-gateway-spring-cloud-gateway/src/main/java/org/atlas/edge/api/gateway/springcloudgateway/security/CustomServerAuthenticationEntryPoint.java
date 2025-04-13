package org.atlas.edge.api.gateway.springcloudgateway.security;

import java.nio.charset.StandardCharsets;
import org.atlas.edge.api.gateway.springcloudgateway.response.Response;
import org.atlas.framework.error.AppError;
import org.atlas.framework.json.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    // Response body
    Response<Void> responseBody = Response.error(AppError.UNAUTHORIZED.getErrorCode(),
        ex.getMessage());
    String responseBodyJson = JsonUtil.getInstance().toJson(responseBody);
    byte[] bytes = responseBodyJson.getBytes(StandardCharsets.UTF_8);

    return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
  }
}
