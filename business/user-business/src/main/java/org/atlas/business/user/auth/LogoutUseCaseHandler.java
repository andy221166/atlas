package org.atlas.business.user.auth;

import lombok.RequiredArgsConstructor;
import org.atlas.port.inbound.user.auth.LogoutUseCase;
import org.atlas.service.user.port.outbound.auth.AuthPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutUseCaseHandler implements LogoutUseCase {

  private final AuthPort authPort;

  @Override
  public Void handle(LogoutInput input) throws Exception {
    authPort.logout(input.getAccessToken());
    return null;
  }
}
