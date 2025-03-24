package org.atlas.service.product.adapter.persistence.jpa.repository;

import java.util.List;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaProductEntity;
import org.atlas.service.product.port.outbound.repository.FindProductCriteria;

public interface CustomJpaProductRepository {

  List<JpaProductEntity> findByCriteria(FindProductCriteria criteria, PagingRequest pagingRequest);

  long countByCriteria(FindProductCriteria criteria);
}
