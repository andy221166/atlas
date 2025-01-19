package org.atlas.service.aggregator.contract.repository.aggregator;

import org.atlas.commons.model.PageDto;

public interface AggOrderRepository {

  PageDto<AggOrder> findByUserId(Integer userId, PagingQuery pagingQuery);

  long countByUserId(Integer userId);

  void insert(AggOrder aggOrder);
}
