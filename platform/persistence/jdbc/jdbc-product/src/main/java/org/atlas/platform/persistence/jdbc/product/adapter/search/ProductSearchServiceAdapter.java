package org.atlas.platform.persistence.jdbc.product.adapter.search;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.persistence.jdbc.product.repository.JdbcProductRepository;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.contract.search.ProductSearchService;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductSearchServiceAdapter implements ProductSearchService {

    private final JdbcProductRepository jdbcProductRepository;

    @Override
    public PageDto<Product> search(SearchProductQuery query) {
        long totalCount = jdbcProductRepository.count(query);
        if (totalCount == 0L) {
            return PageDto.empty();
        }

        List<Product> products = jdbcProductRepository.find(query);

        return PageDto.of(products, totalCount);
    }
}
