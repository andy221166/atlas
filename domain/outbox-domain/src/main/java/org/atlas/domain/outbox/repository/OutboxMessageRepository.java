package org.atlas.domain.outbox.repository;

import java.util.List;
import org.atlas.domain.outbox.entity.OutboxMessageEntity;
import org.atlas.domain.outbox.entity.OutboxMessageStatus;

public interface OutboxMessageRepository {

  List<OutboxMessageEntity> findByStatusOrderByCreatedAt(OutboxMessageStatus status);

  void insert(OutboxMessageEntity outboxMessageEntity);

  void update(OutboxMessageEntity outboxMessageEntity);
}
