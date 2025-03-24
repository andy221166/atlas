package org.atlas.service.product.application.usecase.front;

import java.util.List;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.inbound.usecase.front.SearchProductUseCase;
import org.atlas.service.product.port.inbound.usecase.front.SearchProductUseCase.Output.Product;
import org.atlas.service.product.port.outbound.repository.FindProductParams;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.atlas.service.product.port.outbound.search.SearchParams;
import org.atlas.service.product.port.outbound.search.SearchPort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchProductUseCaseHandler implements SearchProductUseCase {

  private final SearchPort searchPort;
  private final ProductRepositoryPort productRepositoryPort;

  public SearchProductUseCaseHandler(@Nullable SearchPort searchPort,
      ProductRepositoryPort productRepositoryPort) {
    this.searchPort = searchPort;
    this.productRepositoryPort = productRepositoryPort;
  }

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    PagingResult<ProductEntity> productEntityPage;
    if (searchPort != null) {
      SearchParams params = ObjectMapperUtil.getInstance()
          .map(input, SearchParams.class);
      productEntityPage = searchPort.search(params, input.getPagingRequest());
    } else {
      FindProductParams params = ObjectMapperUtil.getInstance()
          .map(input, FindProductParams.class);
      params.setStatus(ProductStatus.IN_STOCK);
      params.setIsActive(true);
      productEntityPage = productRepositoryPort.findAll(params, input.getPagingRequest());
    }
    List<Product> products = productEntityPage.getResults()
        .stream()
        .map(this::map)
        .toList();
    PagingResult<Product> productPage = PagingResult.of(products,
        productEntityPage.getTotalCount());
    return new Output(productPage);
  }

  private Product map(ProductEntity productEntity) {
    Product product = ObjectMapperUtil.getInstance().map(productEntity, Product.class);
    product.setDescription(productEntity.getDetail().getDescription().substring(0, 100));
    return product;
  }
}
