package org.atlas.infrastructure.auth.springsecurityjwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.atlas.infrastructure.api.server.rest.core.response.Response;
import org.atlas.infrastructure.api.server.rest.core.util.HttpServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * AuthenticationEntryPoint is triggered when an unauthenticated user requests a secured HTTP
 * resource.
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception)
      throws IOException {
    if (response.isCommitted()) {
      log.info("Response has already been committed");
      return;
    }

    Response<Void> restResponse = Response.error(HttpStatus.UNAUTHORIZED.value(),
        exception.getMessage());
    HttpServletUtil.respondJson(response, restResponse, HttpStatus.UNAUTHORIZED);
  }
}
