package org.atlas.service.catalog.port.inbound.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.catalog.port.inbound.master.ListCategoryUseCase.Output;

public interface ListCategoryUseCase extends UseCase<Void, Output> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private List<Category> categories;

    @Data
    public static class Category {

      private Integer id;
      private String name;
    }
  }
}
