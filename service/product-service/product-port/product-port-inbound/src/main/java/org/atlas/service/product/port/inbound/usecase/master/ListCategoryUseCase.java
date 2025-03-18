package org.atlas.service.product.port.inbound.usecase.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.port.inbound.usecase.master.ListCategoryUseCase.Output;

public interface ListCategoryUseCase extends UseCase<Void, Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private List<Category> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {

      private Integer id;
      private String name;
    }
  }
}
