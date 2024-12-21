package org.atlas.platform.orm.jpa.product.repository;

import java.util.List;
import org.atlas.platform.orm.jpa.product.entity.JpaProduct;
import org.atlas.service.product.contract.query.SearchProductQuery;

public interface CustomJpaProductRepository {

  List<JpaProduct> find(SearchProductQuery command);

  long count(SearchProductQuery command);
}
