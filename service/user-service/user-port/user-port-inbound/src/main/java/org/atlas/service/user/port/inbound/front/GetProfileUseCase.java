package org.atlas.service.user.port.inbound.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.user.port.inbound.front.GetProfileUseCase.GetProfileOutput;

public interface GetProfileUseCase extends UseCase<Void, GetProfileOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class GetProfileOutput {

    private String firstName;
    private String lastName;
  }
}
