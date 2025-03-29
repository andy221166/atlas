package org.atlas.service.product.port.inbound.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.port.inbound.master.ListBrandUseCase.ListBrandOutput;

public interface ListBrandUseCase extends UseCase<Void, ListBrandOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ListBrandOutput {

    private List<Brand> brands;

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
