package org.atlas.platform.event.core.outbox.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import org.atlas.platform.event.core.outbox.model.OutboxMessage;
import org.atlas.platform.event.core.outbox.model.OutboxMessageStatus;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxMessageRepository extends JpaBaseRepository<OutboxMessage, Long> {

  @Query("SELECT o FROM OutboxMessage o WHERE o.status = :status ORDER BY o.createdAt ASC")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
    // SELECT FOR UPDATE
  List<OutboxMessage> findByStatusWithLock(@Param("status") OutboxMessageStatus status);
}
