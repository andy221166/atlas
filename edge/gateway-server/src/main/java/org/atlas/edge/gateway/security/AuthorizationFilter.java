package org.atlas.edge.gateway.security;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.constant.CustomHeaders;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(AuthorizationRulesProperties.class)
@Slf4j
public class AuthorizationFilter implements GlobalFilter, Ordered {

  private final TokenService tokenService;
  private final AuthorizationRulesProperties authorizationRulesProperties;
  private final PathMatcher pathMatcher;

  public AuthorizationFilter(TokenService tokenService,
      AuthorizationRulesProperties authorizationRulesProperties) {
    this.tokenService = tokenService;
    this.authorizationRulesProperties = authorizationRulesProperties;
    this.pathMatcher = new AntPathMatcher();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    final String requestPath = exchange.getRequest().getPath().value();

    // Check if the request path is non-secured
    boolean isNonSecured = authorizationRulesProperties.getNonSecuredPaths()
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
    TokenInfo tokenInfo;
    try {
      tokenInfo = tokenService.verifyToken(authorizationHeader);
    } catch (Exception e) {
      log.error("Failed to verify token: {}", authorizationHeader, e);
      return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
    }

    // Validate authorization rule using PathMatcher
    boolean isAuthorized = authorizationRulesProperties.getSecuredPathsMap()
        .entrySet()
        .stream()
        .anyMatch(entry -> pathMatcher.match(entry.getKey(), requestPath)
            && entry.getValue().contains(tokenInfo.getRole()));
    if (!isAuthorized) {
      log.error("User {} is not authorized to access {}", tokenInfo.getUserId(), requestPath);
      return onError(exchange, "User is not authorized to access", HttpStatus.FORBIDDEN);
    }

    // Relay claims via headers
    ServerWebExchange modifiedExchange = relayClaims(exchange, tokenInfo);
    return chain.filter(modifiedExchange);
  }


  private ServerWebExchange relayClaims(ServerWebExchange exchange, TokenInfo tokenInfo) {
    ServerHttpRequest modifiedRequest = exchange.getRequest()
        .mutate()
        .header(CustomHeaders.USER_ID, String.valueOf(tokenInfo.getUserId()))
        .header(CustomHeaders.USER_ROLE, tokenInfo.getRole().name())
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
