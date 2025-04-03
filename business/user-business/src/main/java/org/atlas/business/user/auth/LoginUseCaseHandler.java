package org.atlas.business.user.auth;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.port.inbound.user.auth.LoginUseCase;
import org.atlas.service.user.port.outbound.auth.AuthPort;
import org.atlas.service.user.port.outbound.auth.AuthLoginRequest;
import org.atlas.service.user.port.outbound.auth.AuthLoginResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoginUseCaseHandler implements LoginUseCase {

  private final AuthPort authPort;

  @Override
  @Transactional(readOnly = true)
  public LoginOutput handle(LoginInput input) throws Exception {
    AuthLoginRequest authLoginRequest = ObjectMapperUtil.getInstance().map(input, AuthLoginRequest.class);
    AuthLoginResponse authLoginResponse = authPort.login(authLoginRequest);
    return ObjectMapperUtil.getInstance().map(authLoginResponse, LoginOutput.class);
  }
}
