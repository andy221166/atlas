package org.atlas.platform.persistence.jdbc.outbox.repository;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.persistence.jdbc.outbox.supports.OutboxMessageRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcOutboxMessageRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<OutboxMessage> findByStatus(OutboxMessageStatus status) {
    String sql = """
        select om.id, om.event_type, om.payload, om.status, om.processed_at, om.error, om.retries, om.created_at, om.updated_at
        from outbox_message om
        where om.status = :status
        """;

    Map<String, Object> params = Map.of("status", status.name());

    return namedParameterJdbcTemplate.query(sql, params, new OutboxMessageRowMapper());
  }

  public int insert(OutboxMessage outboxMessage) {
    String sql = """
        insert into outbox_message (event_type, payload, status, retries)
        values (:eventType, :payload, :status, :retries)
        """;
    MapSqlParameterSource params = toParams(outboxMessage);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
    Number insertedId = keyHolder.getKey();
    if (insertedId != null) {
      outboxMessage.setId(insertedId.longValue());
    } else {
      throw new RuntimeException("Failed to retrieve the inserted ID");
    }
    return 1;
  }

  public int update(OutboxMessage outboxMessage) {
    String sql = """
        update outbox_message om
        set om.status = :status,
            om.processed_at = :processedAt,
            om.error = :error,
            om.retries = :retries
        where o.id = :id
        """;
    MapSqlParameterSource params = toParams(outboxMessage);
    return namedParameterJdbcTemplate.update(sql, params);
  }

  private MapSqlParameterSource toParams(OutboxMessage outboxMessage) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", outboxMessage.getId());
    params.addValue("eventType", outboxMessage.getEventType());
    params.addValue("payload", outboxMessage.getPayload());
    params.addValue("status", outboxMessage.getStatus().name());
    params.addValue("processedAt", outboxMessage.getProcessedAt());
    params.addValue("error", outboxMessage.getError());
    params.addValue("retries", outboxMessage.getRetries());
    return params;
  }
}
