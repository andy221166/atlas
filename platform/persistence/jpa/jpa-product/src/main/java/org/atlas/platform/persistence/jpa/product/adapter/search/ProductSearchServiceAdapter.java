package org.atlas.platform.persistence.jpa.product.adapter.search;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.persistence.jpa.product.entity.JpaProduct;
import org.atlas.platform.persistence.jpa.product.mapper.ProductMapper;
import org.atlas.platform.persistence.jpa.product.repository.CustomJpaProductRepository;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.contract.search.ProductSearchService;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchServiceAdapter implements ProductSearchService {

    private final CustomJpaProductRepository customJpaProductRepository;

    @Override
    public PageDto<Product> search(SearchProductQuery query) {
        long totalCount = customJpaProductRepository.count(query);
        if (totalCount == 0L) {
            return PageDto.empty();
        }

        List<JpaProduct> jpaProducts = customJpaProductRepository.find(query);
        List<Product> products = jpaProducts.stream()
            .map(ProductMapper::map)
            .toList();

        return PageDto.of(products, totalCount);
    }
}
