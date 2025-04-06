package org.atlas.infrastructure.api.server.rest.core.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.auth.enums.CustomClaim;
import org.atlas.framework.context.UserContext;
import org.atlas.framework.context.UserInfo;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserContextFilter extends OncePerRequestFilter {

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
}
