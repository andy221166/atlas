package org.atlas.platform.api.client.rest.feign.core;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.atlas.commons.constant.CustomHeaders;
import org.atlas.commons.context.CurrentUser;
import org.atlas.commons.context.CurrentUserContext;
import org.springframework.stereotype.Component;

@Component
public class UserContextRequestInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate requestTemplate) {
    CurrentUser currentUser = CurrentUserContext.getCurrentUser();
    if (currentUser != null) {
      requestTemplate.header(CustomHeaders.USER_ID, String.valueOf(currentUser.getUserId()));
      requestTemplate.header(CustomHeaders.USER_ROLE, currentUser.getRole().name());
    }
  }
}
