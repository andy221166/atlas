package org.atlas.edge.gateway.server.springcloud.security;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.jwt.core.JwtData;
import org.atlas.platform.jwt.core.JwtService;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.enums.CustomClaim;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationFilter implements GlobalFilter, Ordered {

  private final JwtService jwtService;
  private final AuthorizationRulesProps authorizationRulesProps;
  private final PathMatcher pathMatcher;

  public AuthorizationFilter(
      JwtService jwtService,
      AuthorizationRulesProps authorizationRulesProps) {
    this.jwtService = jwtService;
    this.authorizationRulesProps = authorizationRulesProps;
    this.pathMatcher = new AntPathMatcher();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    final String requestPath = exchange.getRequest().getPath().value();

    // Check if the request path is non-secured
    boolean isNonSecured = authorizationRulesProps.getNonSecuredPaths()
        .stream()
        .anyMatch(openPath -> pathMatcher.match(openPath, requestPath));
    if (isNonSecured) {
      // Proceed without token verification for open-access paths
      return chain.filter(exchange);
    }

    // Verify token for secured paths
    String authorizationHeader = exchange.getRequest().getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION);
    if (!StringUtils.hasText(authorizationHeader)) {
      log.error("Missing Authorization header");
      return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
    }
    JwtData jwtData;
    try {
      jwtData = jwtService.verify(authorizationHeader, Constant.JWT_ISSUER);
    } catch (Exception e) {
      log.error("Failed to verify token: {}", authorizationHeader, e);
      return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
    }

    // Validate authorization rule using PathMatcher
    boolean isAuthorized = authorizationRulesProps.getSecuredPathsMap()
        .entrySet()
        .stream()
        .anyMatch(entry -> pathMatcher.match(entry.getKey(), requestPath)
            && entry.getValue().contains(jwtData.getUserRole()));
    if (!isAuthorized) {
      log.error("User {} is not authorized to access {}", jwtData.getUserId(), requestPath);
      return onError(exchange, "User is not authorized to access", HttpStatus.FORBIDDEN);
    }

    // Relay claims via headers
    ServerWebExchange modifiedExchange = relayClaims(exchange, jwtData);
    return chain.filter(modifiedExchange);
  }


  private ServerWebExchange relayClaims(ServerWebExchange exchange, JwtData jwtData) {
    ServerHttpRequest modifiedRequest = exchange.getRequest()
        .mutate()
        .header(CustomClaim.USER_ID.getHeader(), String.valueOf(jwtData.getUserId()))
        .header(CustomClaim.USER_ROLE.getHeader(), jwtData.getUserRole().name())
        .build();
    return exchange.mutate()
        .request(modifiedRequest)
        .build();
  }

  private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(status);
    DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Mono.just(buffer));
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
