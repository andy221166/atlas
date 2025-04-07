package org.atlas.domain.product.usecase.internal;

import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler.ListProductInput;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler.ListProductOutput;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler.ListProductOutput.Product;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;
import org.atlas.framework.usecase.input.InternalInput;

@RequiredArgsConstructor
public class InternalListProductUseCaseHandler implements UseCaseHandler<ListProductInput, ListProductOutput> {

  private final ProductRepository productRepository;

  public ListProductOutput handle(ListProductInput input) throws Exception {
    List<ProductEntity> productEntities = productRepository.findByIdIn(input.getIds());
    List<Product> products = ObjectMapperUtil.getInstance()
        .mapList(productEntities, Product.class);
    return new ListProductOutput(products);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  public static class ListProductInput extends InternalInput {

    @NotEmpty
    private List<Integer> ids;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListProductOutput {

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
