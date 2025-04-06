package org.atlas.infrastructure.api.server.rest.core.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class RequestLoggingFilter extends OncePerRequestFilter {

  private static final int MAX_PAYLOAD_LENGTH = 1024 * 1024; // 1 MB in bytes

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    CachedHttpServletRequest cachedRequest = new CachedHttpServletRequest(request);
    RequestLoggingUtil.logRequest(cachedRequest, MAX_PAYLOAD_LENGTH);
    filterChain.doFilter(cachedRequest, response);
  }
}
