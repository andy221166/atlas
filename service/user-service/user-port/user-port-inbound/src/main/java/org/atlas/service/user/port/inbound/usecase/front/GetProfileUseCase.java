package org.atlas.service.user.port.inbound.usecase.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;

public interface GetProfileUseCase
    extends UseCase<Void, GetProfileUseCase.Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private String firstName;
    private String lastName;
  }
}
