package org.atlas.infrastructure.api.server.rest.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.context.UserContext;
import org.atlas.framework.context.UserInfo;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class UserContextFilter extends OncePerRequestFilter {

  private static final Pattern FILTERED_PATHS = Pattern.compile("^/api(/.*)?$");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String userId = request.getHeader(CustomClaim.USER_ID.getHeader());
    String userRole = request.getHeader(CustomClaim.USER_ROLE.getHeader());

    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userRole)) {
      UserInfo userInfo = new UserInfo();
      userInfo.setUserId(Integer.valueOf(userId));
      userInfo.setRole(Role.valueOf(userRole));
      UserContext.set(userInfo);
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      UserContext.clear();
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !FILTERED_PATHS.matcher(request.getRequestURI()).matches();
  }
}
