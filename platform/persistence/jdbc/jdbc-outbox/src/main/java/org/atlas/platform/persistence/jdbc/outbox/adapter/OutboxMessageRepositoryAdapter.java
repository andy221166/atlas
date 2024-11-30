package org.atlas.platform.persistence.jdbc.outbox.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.outbox.repository.OutboxMessageRepository;
import org.atlas.platform.persistence.jdbc.outbox.repository.JdbcOutboxMessageRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMessageRepositoryAdapter implements OutboxMessageRepository {

  private final JdbcOutboxMessageRepository jdbcOutboxMessageRepository;

  @Override
  public List<OutboxMessage> findByStatus(OutboxMessageStatus status) {
    return jdbcOutboxMessageRepository.findByStatus(status);
  }

  @Override
  public void insert(OutboxMessage outboxMessage) {
    jdbcOutboxMessageRepository.insert(outboxMessage);
  }

  @Override
  public void update(OutboxMessage outboxMessage) {
    jdbcOutboxMessageRepository.update(outboxMessage);
  }
}
