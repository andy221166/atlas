package org.atlas.service.product.application.front;

import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.inbound.front.SearchProductUseCase;
import org.atlas.service.product.port.outbound.repository.FindProductCriteria;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.atlas.service.product.port.outbound.search.SearchCriteria;
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
  public SearchProductOutput handle(SearchProductInput input) throws Exception {
    PagingResult<ProductEntity> productEntityPage;
    if (searchPort != null) {
      SearchCriteria criteria = ObjectMapperUtil.getInstance()
          .map(input, SearchCriteria.class);
      productEntityPage = searchPort.search(criteria, input.getPagingRequest());
    } else {
      FindProductCriteria criteria = ObjectMapperUtil.getInstance()
          .map(input, FindProductCriteria.class);
      criteria.setStatus(ProductStatus.IN_STOCK);
      criteria.setIsActive(true);
      productEntityPage = productRepositoryPort.findByCriteria(criteria, input.getPagingRequest());
    }

    PagingResult<SearchProductOutput.Product> productPage = ObjectMapperUtil.getInstance()
        .mapPage(productEntityPage, SearchProductOutput.Product.class);
    return new SearchProductOutput(productPage.getResults(), productPage.getPagination());
  }
}
