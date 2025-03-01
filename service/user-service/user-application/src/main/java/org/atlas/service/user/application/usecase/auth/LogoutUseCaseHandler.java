package org.atlas.service.user.application.usecase.auth;

import lombok.RequiredArgsConstructor;
import org.atlas.service.user.port.inbound.auth.LogoutUseCase;
import org.atlas.service.user.port.outbound.auth.AuthService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutUseCaseHandler implements LogoutUseCase {

  private final AuthService authService;

  @Override
  public Void handle(Input input) throws Exception {
    authService.logout(input.getAccessToken());
    return null;
  }
}
