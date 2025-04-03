package org.atlas.platform.event.gateway.outbox.repository;

import java.util.List;
import org.atlas.platform.event.gateway.outbox.model.OutboxMessage;
import org.atlas.platform.event.gateway.outbox.model.OutboxMessageStatus;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxMessageRepository extends JpaBaseRepository<OutboxMessage, Long> {

  List<OutboxMessage> findByStatusOrderByCreatedAt(@Param("status") OutboxMessageStatus status);
}
