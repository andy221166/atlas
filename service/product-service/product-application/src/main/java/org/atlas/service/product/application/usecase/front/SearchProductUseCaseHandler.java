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
    List<Product> products = ObjectMapperUtil.getInstance()
        .mapList(productEntityPage.getResults(), Product.class);
    return new Output(products, productEntityPage.getTotalCount());
  }
}
