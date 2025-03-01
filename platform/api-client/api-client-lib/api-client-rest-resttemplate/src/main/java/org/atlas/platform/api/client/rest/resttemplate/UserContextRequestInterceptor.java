package org.atlas.platform.api.client.rest.resttemplate;

import java.io.IOException;
import org.atlas.commons.context.CurrentUser;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserContextRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    CurrentUser currentUser = UserCon.getCurrentUser();
    if (currentUser != null) {
      request.getHeaders().add(CustomHeaders.USER_ID, String.valueOf(currentUser.getUserId()));
      request.getHeaders().add(CustomHeaders.USER_ROLE, currentUser.getRole().name());
    }
    return execution.execute(request, body);
  }
}
