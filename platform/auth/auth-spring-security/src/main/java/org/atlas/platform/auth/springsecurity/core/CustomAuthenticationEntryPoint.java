package org.atlas.platform.auth.springsecurity.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.rest.server.core.response.RestResponse;
import org.atlas.platform.rest.server.core.util.HttpServletUtil;
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

    RestResponse<Void> restResponse = RestResponse.error(HttpStatus.UNAUTHORIZED.value(),
        exception.getMessage());
    HttpServletUtil.respondJson(response, restResponse, HttpStatus.UNAUTHORIZED);
  }
}
