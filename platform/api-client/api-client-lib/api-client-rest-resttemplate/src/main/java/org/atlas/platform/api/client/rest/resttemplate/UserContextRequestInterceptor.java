package org.atlas.platform.api.client.rest.resttemplate;

import java.io.IOException;
import org.atlas.platform.commons.context.CurrentUser;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.enums.CustomClaim;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserContextRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    CurrentUser currentUser = UserContext.get();
    if (currentUser != null) {
      request.getHeaders().add(CustomClaim.USER_ID.getHeader(),
          String.valueOf(currentUser.getUserId()));
      request.getHeaders().add(CustomClaim.USER_ROLE.getHeader(),
          currentUser.getRole().name());
    }
    return execution.execute(request, body);
  }
}
