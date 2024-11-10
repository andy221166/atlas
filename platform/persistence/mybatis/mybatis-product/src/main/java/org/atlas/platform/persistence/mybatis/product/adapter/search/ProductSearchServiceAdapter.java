package org.atlas.platform.persistence.mybatis.product.adapter.search;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.persistence.mybatis.product.mapper.ProductMapper;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.contract.search.ProductSearchService;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductSearchServiceAdapter implements ProductSearchService {

    private final ProductMapper productMapper;

    @Override
    public PageDto<Product> search(SearchProductQuery query) {
        long totalCount = productMapper.count(query);
        if (totalCount == 0L) {
            return PageDto.empty();
        }

        List<Product> products = productMapper.find(query);

        return PageDto.of(products, totalCount);
    }
}
