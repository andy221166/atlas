package org.atlas.infrastructure.api.client.core.rest.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.atlas.framework.context.UserContext;
import org.atlas.framework.context.UserInfo;
import org.atlas.framework.security.enums.CustomClaim;
import org.springframework.stereotype.Component;

@Component
public class UserContextRequestInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate requestTemplate) {
    UserInfo userInfo = UserContext.get();
    if (userInfo != null) {
      requestTemplate.header(CustomClaim.USER_ID.getHeader(),
          String.valueOf(userInfo.getUserId()));
      requestTemplate.header(CustomClaim.USER_ROLE.getHeader(),
          userInfo.getRole().name());
    }
  }
}
