package org.atlas.platform.api.client.rest.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.atlas.platform.commons.context.UserInfo;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.enums.CustomClaim;
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
