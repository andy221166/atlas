package org.atlas.platform.persistence.jpa.product.repository;

import java.util.List;
import org.atlas.platform.persistence.jpa.product.entity.JpaProduct;
import org.atlas.service.product.contract.query.SearchProductQuery;

public interface CustomJpaProductRepository {

  List<JpaProduct> find(SearchProductQuery command);

  long count(SearchProductQuery command);
}
