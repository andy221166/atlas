package org.atlas.platform.api.server.rest.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.atlas.platform.commons.context.CurrentUser;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.enums.CustomClaim;
import org.atlas.platform.commons.enums.Role;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String userId = request.getHeader(CustomClaim.USER_ID.getHeader());
    String userRole = request.getHeader(CustomClaim.USER_ROLE.getHeader());

    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userRole)) {
      CurrentUser currentUser = new CurrentUser();
      currentUser.setUserId(Integer.valueOf(userId));
      currentUser.setRole(Role.valueOf(userRole));
      UserContext.set(currentUser);
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      UserContext.clear();
    }
  }
}
