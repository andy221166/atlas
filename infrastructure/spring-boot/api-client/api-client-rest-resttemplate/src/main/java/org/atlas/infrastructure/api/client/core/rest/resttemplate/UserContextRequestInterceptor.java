package org.atlas.infrastructure.api.client.core.rest.resttemplate;

import java.io.IOException;
import org.atlas.framework.security.enums.CustomClaim;
import org.atlas.framework.security.UserContext;
import org.atlas.framework.security.model.UserInfo;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserContextRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    UserInfo userInfo = UserContext.get();
    if (userInfo != null) {
      request.getHeaders().add(CustomClaim.USER_ID.getHeader(),
          String.valueOf(userInfo.getUserId()));
      request.getHeaders().add(CustomClaim.USER_ROLE.getHeader(),
          userInfo.getRole().name());
    }
    return execution.execute(request, body);
  }
}
