package org.atlas.infrastructure.api.server.rest.core.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.context.ContextInfo;
import org.atlas.framework.context.Contexts;
import org.atlas.framework.security.enums.CustomClaim;
import org.atlas.infrastructure.api.server.rest.core.util.HttpUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class ContextFilter extends OncePerRequestFilter {

  // Only apply context filter to /api/** routes
  private static final Pattern FILTERED_PATHS = Pattern.compile("^/api(/.*)?$");

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    ContextInfo contextInfo = new ContextInfo();

    // sessionId
    contextInfo.setSessionId(
        HttpUtil.getHeader(request, CustomClaim.SESSION_ID.getHeader()));

    // userId
    String userId = HttpUtil.getHeader(request, CustomClaim.USER_ID.getHeader());
    if (userId != null) {
      contextInfo.setUserId(Integer.parseInt(userId));
    }

    // userRole
    String userRole = HttpUtil.getHeader(request, CustomClaim.USER_ROLE.getHeader());
    if (userRole != null) {
      contextInfo.setUserRole(Role.valueOf(userRole));
    }

    // deviceId
    contextInfo.setDeviceId(HttpUtil.getHeader(request, "X-Device-Id"));

    // expiresAt
    String expiresAt = HttpUtil.getHeader(request, CustomClaim.EXPIRES_AT.getHeader());
    if (expiresAt != null) {
      contextInfo.setExpiresAt(new Date(Long.parseLong(expiresAt)));
    }

    // Set context into thread-local for current request
    Contexts.set(contextInfo);

    try {
      filterChain.doFilter(request, response);
    } finally {
      // Clean up to prevent memory leak
      Contexts.clear();
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // Only apply the filter if the URI matches /api/**
    return !FILTERED_PATHS.matcher(request.getRequestURI()).matches();
  }
}
