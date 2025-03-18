package org.atlas.service.product.port.inbound.usecase.internal;

import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.platform.usecase.port.input.InternalInput;

public interface ListProductUseCase
    extends UseCase<ListProductUseCase.Input, ListProductUseCase.Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  class Input extends InternalInput {

    @NotEmpty
    private List<Integer> ids;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private List<Product> products;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

      private Integer id;
      private String name;
      private BigDecimal price;
    }
  }
}
