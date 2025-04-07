package org.atlas.domain.user.usecase.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.auth.AuthLoginUseCaseHandler.LoginInput;
import org.atlas.domain.user.usecase.auth.AuthLoginUseCaseHandler.LoginOutput;
import org.atlas.framework.auth.AuthPort;
import org.atlas.framework.auth.model.LoginRequest;
import org.atlas.framework.auth.model.LoginResponse;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AuthLoginUseCaseHandler implements UseCaseHandler<LoginInput, LoginOutput> {

  private final AuthPort authPort;

  @Override
  public LoginOutput handle(LoginInput input) throws Exception {
    LoginRequest request = ObjectMapperUtil.getInstance().map(input, LoginRequest.class);
    LoginResponse response = authPort.login(request);
    return ObjectMapperUtil.getInstance()
        .map(response, LoginOutput.class);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LoginInput {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LoginOutput {

    private String accessToken;
    private String refreshToken;
  }
}
