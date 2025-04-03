package org.atlas.port.inbound.product.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.port.inbound.product.front.GetProductUseCase.GetProductInput;
import org.atlas.port.inbound.product.front.GetProductUseCase.GetProductOutput;

public interface GetProductUseCase
    extends UseCase<GetProductInput, GetProductOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class GetProductInput {

    private Integer id;
  }

  /**
   * Implement {@link Serializable} to support caching
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class GetProductOutput implements Serializable {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String description;
    private Map<String, String> attributes;
    private String brand;
    private List<String> categories;
  }
}
