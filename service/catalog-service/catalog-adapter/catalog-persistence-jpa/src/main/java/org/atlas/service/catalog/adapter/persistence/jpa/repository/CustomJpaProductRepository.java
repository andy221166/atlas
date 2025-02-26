package org.atlas.service.catalog.adapter.persistence.jpa.repository;

import java.util.List;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.catalog.adapter.persistence.jpa.entity.JpaProduct;
import org.atlas.service.catalog.port.outbound.repository.FindProductCriteria;

public interface CustomJpaProductRepository {

  List<JpaProduct> find(FindProductCriteria criteria, PagingRequest pagingRequest);

  long count(FindProductCriteria criteria);
}
