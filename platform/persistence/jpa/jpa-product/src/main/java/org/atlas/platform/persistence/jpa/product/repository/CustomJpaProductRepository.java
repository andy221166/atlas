package org.atlas.platform.persistence.jpa.product.repository;

import org.atlas.platform.persistence.jpa.product.entity.JpaProduct;
import org.atlas.service.product.contract.query.SearchProductQuery;

import java.util.List;

public interface CustomJpaProductRepository {

    List<JpaProduct> find(SearchProductQuery command);
    long count(SearchProductQuery command);
}
