package org.atlas.platform.outbox.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;

public interface OutboxMessageRepository {

  Optional<OutboxMessage> findById(Long id);
  List<OutboxMessage> findByStatus(OutboxMessageStatus status);
  void insert(OutboxMessage outboxMessage);
  void update(OutboxMessage outboxMessage);
}
