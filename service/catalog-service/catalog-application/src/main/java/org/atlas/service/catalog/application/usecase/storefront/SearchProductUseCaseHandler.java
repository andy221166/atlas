package org.atlas.service.catalog.application.usecase.storefront;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.atlas.service.catalog.domain.entity.ProductImageEntity;
import org.atlas.service.catalog.port.inbound.storefront.SearchProductUseCase;
import org.atlas.service.catalog.port.inbound.storefront.SearchProductUseCase.Output.Product;
import org.atlas.service.catalog.port.outbound.repository.FindProductCriteria;
import org.atlas.service.catalog.port.outbound.repository.ProductRepository;
import org.atlas.service.catalog.port.outbound.search.SearchProductCriteria;
import org.atlas.service.catalog.port.outbound.search.SearchService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchProductUseCaseHandler implements SearchProductUseCase {

    private final SearchService searchService;
    private final ProductRepository productRepository;

    public SearchProductUseCaseHandler(@Nullable SearchService searchService,
                                       ProductRepository productRepository) {
        this.searchService = searchService;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Output handle(Input input) throws Exception {
      PagingResult<ProductEntity> productEntityPage;
      if (searchService != null) {
        SearchProductCriteria criteria = ObjectMapperUtil.getInstance().map(input, SearchProductCriteria.class);
        productEntityPage = searchService.search(criteria, input.getPagingRequest());
      } else {
        FindProductCriteria criteria = ObjectMapperUtil.getInstance().map(input, FindProductCriteria.class);
        productEntityPage = productRepository.findByCriteria(criteria, input.getPagingRequest());
      }
      List<Product> products = productEntityPage.getResults()
              .stream()
              .map(this::map)
              .toList();
      PagingResult<Product> productPage = PagingResult.of(products, productEntityPage.getTotalCount());
      return new Output(productPage);
    }

    private Product map(ProductEntity productEntity) {
      Product product = ObjectMapperUtil.getInstance().map(productEntity, Product.class);
      product.setDescription(productEntity.getDetail().getDescription().substring(0, 100));
      List<ProductImageEntity> productImageEntities = productEntity.getImages();
      if (CollectionUtils.isNotEmpty(productImageEntities)) {
        product.setImageUrl(productImageEntities.get(0).getImageUrl());
      }
      return product;
    }
}
