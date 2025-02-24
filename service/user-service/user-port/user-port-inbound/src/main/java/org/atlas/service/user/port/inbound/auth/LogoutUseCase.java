package org.atlas.service.user.port.inbound.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;

public interface LogoutUseCase
    extends UseCase<LogoutUseCase.Input, Void> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  class Input {

    @NotBlank
    private String accessToken;
  }
}
