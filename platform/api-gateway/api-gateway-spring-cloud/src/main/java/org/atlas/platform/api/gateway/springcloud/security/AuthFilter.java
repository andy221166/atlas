package org.atlas.platform.api.gateway.springcloud.security;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.gateway.springcloud.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.enums.CustomClaim;
import org.atlas.platform.json.JsonUtil;
import org.atlas.platform.jwt.core.JwtData;
import org.atlas.platform.jwt.core.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class AuthFilter implements GlobalFilter, Ordered {

  private final JwtService jwtService;
  private final AuthRulesProps authRulesProps;
  private final PathMatcher pathMatcher;

  public AuthFilter(
      JwtService jwtService,
      AuthRulesProps authRulesProps) {
    this.jwtService = jwtService;
    this.authRulesProps = authRulesProps;
    this.pathMatcher = new AntPathMatcher();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    final String requestPath = exchange.getRequest().getPath().value();

    // Check if the request path is non-secured
    boolean isNonSecured = authRulesProps.getNonSecuredPaths()
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
      return onError(exchange,
          Response.error(AppError.UNAUTHORIZED.getErrorCode(), "Missing Authorization header"),
          HttpStatus.UNAUTHORIZED);
    }
    JwtData jwtData;
    try {
      jwtData = jwtService.verify(authorizationHeader, Constant.JWT_ISSUER);
    } catch (Exception e) {
      log.error("Failed to verify token: {}", authorizationHeader, e);
      return onError(exchange,
          Response.error(AppError.UNAUTHORIZED.getErrorCode(), "Invalid access token"),
          HttpStatus.UNAUTHORIZED);
    }

    // Validate authorization rule using PathMatcher
    boolean isAuthorized = authRulesProps.getSecuredPathsMap()
        .entrySet()
        .stream()
        .anyMatch(entry -> pathMatcher.match(entry.getKey(), requestPath)
            && entry.getValue().contains(jwtData.getUserRole()));
    if (!isAuthorized) {
      log.error("User {} is not authorized to access {}", jwtData.getUserId(), requestPath);
      return onError(exchange,
          Response.error(AppError.PERMISSION_DENIED.getErrorCode(), "User is not authorized to access"),
          HttpStatus.FORBIDDEN);
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

  private Mono<Void> onError(ServerWebExchange exchange, Response<Void> response,
      HttpStatus status) {
    ServerHttpResponse httpResponse = exchange.getResponse();
    httpResponse.setStatusCode(status);
    httpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    byte[] bytes = JsonUtil.getInstance().toJson(response).getBytes(StandardCharsets.UTF_8);
    DataBuffer buffer = httpResponse.bufferFactory().wrap(bytes);

    return httpResponse.writeWith(Mono.just(buffer));
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
