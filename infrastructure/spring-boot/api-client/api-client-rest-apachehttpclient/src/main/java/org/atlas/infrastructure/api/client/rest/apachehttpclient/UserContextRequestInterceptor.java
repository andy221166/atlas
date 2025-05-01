package org.atlas.infrastructure.api.client.rest.apachehttpclient;

import java.io.IOException;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.atlas.framework.security.session.SessionContext;
import org.atlas.framework.security.session.SessionInfo;
import org.atlas.framework.security.enums.CustomClaim;

public class UserContextRequestInterceptor implements HttpRequestInterceptor {

  @Override
  public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext)
      throws HttpException, IOException {
    SessionInfo sessionInfo = SessionContext.get();
    if (sessionInfo != null) {
      httpRequest.addHeader(CustomClaim.SESSION_ID.getHeader(), sessionInfo.getSessionId());
      httpRequest.addHeader(CustomClaim.USER_ID.getHeader(), sessionInfo.getUserId());
      httpRequest.addHeader(CustomClaim.USER_ROLE.getHeader(), sessionInfo.getUserRole());
    }
  }
}
