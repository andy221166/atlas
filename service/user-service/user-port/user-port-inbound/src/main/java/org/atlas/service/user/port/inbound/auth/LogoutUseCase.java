package org.atlas.service.user.port.inbound.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.user.port.inbound.auth.LogoutUseCase.LogoutInput;

public interface LogoutUseCase extends UseCase<LogoutInput, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class LogoutInput {

    @NotBlank
    private String accessToken;
  }
}
