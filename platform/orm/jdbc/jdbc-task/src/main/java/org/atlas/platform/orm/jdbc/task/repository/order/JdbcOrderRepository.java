package org.atlas.platform.orm.jdbc.task.repository.order;

import java.util.Date;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public JdbcOrderRepository(
      @Qualifier("orderNamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public int cancelOverdueProcessingOrders(OrderStatus canceledStatus, String canceledReason,
      OrderStatus processingStatus, Date endDate) {
    String sql = """
        update orders o
        set o.status = :canceledStatus, o.canceled_reason = :canceledReason
        where o.status = :processingStatus
          and o.created_at < :endDate
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("canceledStatus", canceledStatus);
    params.addValue("canceledReason", canceledReason);
    params.addValue("processingStatus", processingStatus);
    params.addValue("endDate", endDate);
    return namedParameterJdbcTemplate.update(sql, params);
  }
}
