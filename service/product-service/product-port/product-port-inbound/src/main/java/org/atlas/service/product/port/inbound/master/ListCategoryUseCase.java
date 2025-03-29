package org.atlas.service.product.port.inbound.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.port.inbound.master.ListCategoryUseCase.ListCategoryOutput;

public interface ListCategoryUseCase extends UseCase<Void, ListCategoryOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ListCategoryOutput {

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
