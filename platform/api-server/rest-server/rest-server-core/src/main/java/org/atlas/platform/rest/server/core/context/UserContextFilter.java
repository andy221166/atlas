package org.atlas.platform.rest.server.core.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.atlas.commons.constant.CustomHeaders;
import org.atlas.commons.context.CurrentUser;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.service.user.domain.shared.enums.Role;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserContextFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String userId = request.getHeader(CustomHeaders.USER_ID);
    String userRole = request.getHeader(CustomHeaders.USER_ROLE);

    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userRole)) {
      CurrentUser currentUser = new CurrentUser();
      currentUser.setUserId(Integer.valueOf(userId));
      currentUser.setRole(Role.valueOf(userRole));
      CurrentUserContext.setCurrentUser(currentUser);
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      CurrentUserContext.clear();
    }
  }
}
