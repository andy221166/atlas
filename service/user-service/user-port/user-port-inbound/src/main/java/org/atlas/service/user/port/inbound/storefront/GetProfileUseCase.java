package org.atlas.service.user.port.inbound.storefront;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.usecase.port.UseCase;

public interface GetProfileUseCase
    extends UseCase<Void, GetProfileUseCase.Output> {

  @Data
  @EqualsAndHashCode(callSuper = false)
  class Output {

    private String firstName;
    private String lastName;
  }
}
