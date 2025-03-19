package org.atlas.service.product.adapter.persistence.jpa.repository;

import java.util.List;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaProductEntity;
import org.atlas.service.product.port.outbound.repository.FindProductParams;

public interface CustomJpaProductRepository {

  List<JpaProductEntity> find(FindProductParams params, PagingRequest pagingRequest);

  long count(FindProductParams params);
}
