package org.atlas.infrastructure.api.client.rest.resttemplate;

import java.io.IOException;
import org.atlas.framework.security.session.SessionContext;
import org.atlas.framework.security.session.SessionInfo;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserContextRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    SessionInfo sessionInfo = SessionContext.get();
    if (sessionInfo != null) {
      request.getHeaders().add(CustomClaim.SESSION_ID.getHeader(),
          sessionInfo.getSessionId());
      request.getHeaders().add(CustomClaim.USER_ID.getHeader(),
          String.valueOf(sessionInfo.getUserId()));
      request.getHeaders().add(CustomClaim.USER_ROLE.getHeader(),
          sessionInfo.getUserRole().name());
    }
    return execution.execute(request, body);
  }
}
