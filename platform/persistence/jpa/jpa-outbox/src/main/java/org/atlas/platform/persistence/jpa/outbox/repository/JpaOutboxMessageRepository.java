package org.atlas.platform.persistence.jpa.outbox.repository;

import java.util.List;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.persistence.jpa.outbox.entity.JpaOutboxMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOutboxMessageRepository extends JpaBaseRepository<JpaOutboxMessage, Long> {

  List<JpaOutboxMessage> findByStatus(OutboxMessageStatus status);
}
