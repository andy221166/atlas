package org.atlas.infrastructure.api.server.rest.core.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(0)
public class RequestLoggingFilter extends OncePerRequestFilter {

  private static final Pattern FILTERED_PATHS = Pattern.compile("^/api(/.*)?$");
  private static final int MAX_PAYLOAD_LENGTH = 1024 * 1024; // 1 MB in bytes

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    CachedHttpServletRequest cachedRequest = new CachedHttpServletRequest(request);
    RequestLoggingUtil.logRequest(cachedRequest, MAX_PAYLOAD_LENGTH);
    filterChain.doFilter(cachedRequest, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !FILTERED_PATHS.matcher(request.getRequestURI()).matches();
  }
}
