package org.atlas.platform.orm.jpa.outbox.repository;

import java.util.List;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.outbox.entity.JpaOutboxMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOutboxMessageRepository extends JpaBaseRepository<JpaOutboxMessage, Long> {

  List<JpaOutboxMessage> findByStatus(OutboxMessageStatus status);
}
