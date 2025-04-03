package org.atlas.port.inbound.product.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.port.inbound.product.master.ListBrandUseCase.ListBrandOutput;

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
