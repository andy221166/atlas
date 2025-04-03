package org.atlas.platform.api.client.rest.apachehttpclient;

import java.io.IOException;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.context.UserInfo;
import org.atlas.platform.commons.enums.CustomClaim;

public class UserContextRequestInterceptor implements HttpRequestInterceptor {

  @Override
  public void process(HttpRequest httpRequest, EntityDetails entityDetails, HttpContext httpContext)
      throws HttpException, IOException {
    UserInfo userInfo = UserContext.get();
    if (userInfo != null) {
      httpRequest.addHeader(CustomClaim.USER_ID.getHeader(), userInfo.getUserId());
      httpRequest.addHeader(CustomClaim.USER_ROLE.getHeader(), userInfo.getRole());
    }
  }
}
