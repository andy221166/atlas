package org.atlas.infrastructure.api.client.rest.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.atlas.framework.security.session.SessionContext;
import org.atlas.framework.security.session.SessionInfo;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.stereotype.Component;

@Component
public class UserContextRequestInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate requestTemplate) {
    SessionInfo sessionInfo = SessionContext.get();
    if (sessionInfo != null) {
      requestTemplate.header(CustomClaim.USER_ID.getHeader(),
          String.valueOf(sessionInfo.getUserId()));
      requestTemplate.header(CustomClaim.USER_ROLE.getHeader(),
          sessionInfo.getUserRole().name());
      requestTemplate.header(CustomClaim.SESSION_ID.getHeader(),
          sessionInfo.getSessionId());
    }
  }
}
