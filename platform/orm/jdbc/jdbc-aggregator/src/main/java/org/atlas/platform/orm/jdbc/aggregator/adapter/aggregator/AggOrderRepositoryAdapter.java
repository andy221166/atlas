package org.atlas.platform.orm.jdbc.aggregator.adapter.aggregator;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.orm.jdbc.aggregator.repository.aggregator.JdbcAggOrderRepository;
import org.atlas.service.aggregator.contract.repository.aggregator.AggOrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AggOrderRepositoryAdapter implements AggOrderRepository {

  private final JdbcAggOrderRepository jdbcAggOrderRepository;

  @Override
  public PageDto<AggOrder> findByUserId(Integer userId, PagingQuery pagingQuery) {
    long totalCount = jdbcAggOrderRepository.countByUserId(userId);
    if (totalCount == 0L) {
      return PageDto.empty();
    }
    List<AggOrder> aggOrders = jdbcAggOrderRepository.findByUserId(userId, pagingQuery);
    return PageDto.of(aggOrders, totalCount);
  }

  @Override
  public long countByUserId(Integer userId) {
    return jdbcAggOrderRepository.countByUserId(userId);
  }

  @Override
  public void insert(AggOrder aggOrder) {
    jdbcAggOrderRepository.insert(aggOrder);
  }
}
