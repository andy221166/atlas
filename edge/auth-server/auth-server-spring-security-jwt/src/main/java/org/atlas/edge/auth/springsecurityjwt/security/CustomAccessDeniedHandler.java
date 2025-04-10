package org.atlas.edge.auth.springsecurityjwt.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.atlas.infrastructure.api.server.rest.core.response.Response;
import org.atlas.infrastructure.api.server.rest.core.util.HttpServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * AccessDecisionHandler is triggered when a user is authenticated but not authorized to access the
 * given resource.
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException exception)
      throws IOException, ServletException {
    if (response.isCommitted()) {
      log.info("Response has already been committed");
      return;
    }

    Response<Void> restResponse = Response.error(HttpStatus.FORBIDDEN.value(),
        exception.getMessage());
    HttpServletUtil.respondJson(response, restResponse, HttpStatus.FORBIDDEN);
  }
}
