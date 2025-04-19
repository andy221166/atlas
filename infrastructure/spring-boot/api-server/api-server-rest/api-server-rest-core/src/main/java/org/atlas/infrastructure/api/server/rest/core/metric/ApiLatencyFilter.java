package org.atlas.infrastructure.api.server.rest.core.metric;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.observability.metrics.ApiLatencyPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ApiLatencyFilter extends OncePerRequestFilter {

  private final ApplicationConfigPort applicationConfigPort;
  private final ApiLatencyPublisher apiLatencyPublisher;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    long start = System.currentTimeMillis();
    try {
      filterChain.doFilter(request, response);
    } finally {
      long elapsedTimeMs = System.currentTimeMillis() - start;
      String service = applicationConfigPort.getApplicationName();
      String endpoint = request.getRequestURI();
      String method = request.getMethod();
      int httpStatus = response.getStatus();
      String channel = Optional.ofNullable(request.getHeader("X-Channel")).orElse("unknown");
      apiLatencyPublisher.publish(service, endpoint, method, httpStatus, channel, elapsedTimeMs);
    }
  }
}
