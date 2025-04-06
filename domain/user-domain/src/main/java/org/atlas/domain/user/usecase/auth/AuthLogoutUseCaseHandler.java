package org.atlas.domain.user.usecase.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.auth.AuthLogoutUseCaseHandler.LogoutInput;
import org.atlas.framework.auth.AuthPort;
import org.atlas.framework.usecase.UseCaseHandler;

@RequiredArgsConstructor
public class AuthLogoutUseCaseHandler implements UseCaseHandler<LogoutInput, Void> {

  private final AuthPort authPort;

  @Override
  public Void handle(LogoutInput input) throws Exception {
    authPort.logout(input.getAccessToken());
    return null;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LogoutInput {

    @NotBlank
    private String accessToken;
  }
}
