package org.atlas.port.inbound.product.internal;

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
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductInput;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductOutput;

public interface ListProductUseCase
    extends UseCase<ListProductInput, ListProductOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  class ListProductInput extends InternalInput {

    @NotEmpty
    private List<Integer> ids;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ListProductOutput {

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
