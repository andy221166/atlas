package org.atlas.service.user.application.usecase.auth;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.port.inbound.usecase.auth.LoginUseCase;
import org.atlas.service.user.port.outbound.auth.AuthService;
import org.atlas.service.user.port.outbound.auth.LoginRequest;
import org.atlas.service.user.port.outbound.auth.LoginResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoginUseCaseHandler implements LoginUseCase {

  private final AuthService authService;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    LoginRequest request = ObjectMapperUtil.getInstance().map(input, LoginRequest.class);
    LoginResponse response = authService.login(request);
    return ObjectMapperUtil.getInstance().map(response, Output.class);
  }
}
