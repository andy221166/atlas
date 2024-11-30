package org.atlas.platform.persistence.jdbc.outbox.supports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.persistence.jdbc.core.NullSafeRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class OutboxMessageRowMapper implements RowMapper<OutboxMessage> {

  @Override
  public OutboxMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
    NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);
    OutboxMessage outboxMessage = new OutboxMessage();
    outboxMessage.setId(rowMapper.getLong("id"));
    outboxMessage.setEventType(EventType.valueOf(rowMapper.getString("event_type")));
    outboxMessage.setPayload(rowMapper.getString("payload"));
    outboxMessage.setStatus(OutboxMessageStatus.valueOf(rowMapper.getString("status")));
    outboxMessage.setProcessedAt(rowMapper.getTimestamp("processed_at"));
    outboxMessage.setError(rowMapper.getString("error"));
    outboxMessage.setRetries(rowMapper.getInt("retries"));
    outboxMessage.setCreatedAt(rowMapper.getTimestamp("created_at"));
    outboxMessage.setUpdatedAt(rowMapper.getTimestamp("updated_at"));
    return outboxMessage;
  }
}
