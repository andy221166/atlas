package org.atlas.service.product.port.inbound.usecase.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.port.inbound.usecase.master.ListBrandUseCase.Output;

public interface ListBrandUseCase extends UseCase<Void, Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private List<Output.Brand> brands;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Brand {

      private Integer id;
      private String name;
    }
  }
}
