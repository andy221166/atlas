package org.atlas.service.catalog.adapter.persistence.jpa.repository;

import java.util.List;
import org.atlas.service.catalog.adapter.persistence.jpa.entity.JpaProduct;
import org.atlas.service.product.contract.query.SearchProductQuery;

public interface CustomJpaProductRepository {

  List<JpaProduct> find(SearchProductQuery command);

  long count(SearchProductQuery command);
}
