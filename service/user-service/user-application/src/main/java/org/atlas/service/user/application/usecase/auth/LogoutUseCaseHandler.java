package org.atlas.service.user.application.usecase.auth;

import lombok.RequiredArgsConstructor;
import org.atlas.service.user.port.inbound.usecase.auth.LogoutUseCase;
import org.atlas.service.user.port.outbound.auth.AuthPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutUseCaseHandler implements LogoutUseCase {

  private final AuthPort authPort;

  @Override
  public Void handle(Input input) throws Exception {
    authPort.logout(input.getAccessToken());
    return null;
  }
}
