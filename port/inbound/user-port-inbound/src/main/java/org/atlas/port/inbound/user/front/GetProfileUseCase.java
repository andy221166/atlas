package org.atlas.port.inbound.user.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.port.inbound.user.front.GetProfileUseCase.GetProfileOutput;

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
