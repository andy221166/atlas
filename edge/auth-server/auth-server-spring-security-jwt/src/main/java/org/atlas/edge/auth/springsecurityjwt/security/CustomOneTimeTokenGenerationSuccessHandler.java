package org.atlas.edge.auth.springsecurityjwt.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;

@RequiredArgsConstructor
@Slf4j
public class CustomOneTimeTokenGenerationSuccessHandler implements
    OneTimeTokenGenerationSuccessHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      OneTimeToken oneTimeToken) throws IOException, ServletException {
    String username = oneTimeToken.getUsername();
    String tokenValue = oneTimeToken.getTokenValue();
    log.info("Generated one-time token {} successfully for username {}", tokenValue, username);
    // TODO: Send notification
  }
}
