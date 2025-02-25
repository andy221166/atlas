package org.atlas.service.catalog.adapter.persistence.jpa.repository;

import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.catalog.adapter.persistence.jpa.entity.JpaProduct;
import org.atlas.service.catalog.port.outbound.repository.FindProductCriteria;

import java.util.List;

public interface CustomJpaProductRepository {

  List<JpaProduct> find(FindProductCriteria criteria, PagingRequest pagingRequest);

  long count(FindProductCriteria criteria);
}
