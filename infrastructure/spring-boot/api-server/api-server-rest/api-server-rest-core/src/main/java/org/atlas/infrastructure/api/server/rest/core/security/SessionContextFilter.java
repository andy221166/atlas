package org.atlas.infrastructure.api.server.rest.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.security.session.SessionContext;
import org.atlas.framework.security.session.SessionInfo;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class SessionContextFilter extends OncePerRequestFilter {

  private static final Pattern FILTERED_PATHS = Pattern.compile("^/api(/.*)?$");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String userId = request.getHeader(CustomClaim.USER_ID.getHeader());
    String userRole = request.getHeader(CustomClaim.USER_ROLE.getHeader());
    String sessionId = request.getHeader(CustomClaim.SESSION_ID.getHeader());
    String expiresAt = request.getHeader(CustomClaim.EXPIRES_AT.getHeader());

    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userRole)) {
      SessionInfo sessionInfo = new SessionInfo();
      sessionInfo.setSessionId(sessionId);
      sessionInfo.setUserId(Integer.valueOf(userId));
      sessionInfo.setUserRole(Role.valueOf(userRole));
      sessionInfo.setExpiresAt(new Date(Long.parseLong(expiresAt)));
      SessionContext.set(sessionInfo);
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      SessionContext.clear();
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !FILTERED_PATHS.matcher(request.getRequestURI()).matches();
  }
}
