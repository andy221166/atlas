package org.atlas.platform.event.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.event.DomainEvent;
import org.atlas.platform.lock.core.LockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventIdempotenceService {

  private final LockService lockService;

  @Value("${spring.application.name}")
  private String applicationName;

  /**
   * Checks if a given domain event is new (not yet processed).
   */
  public boolean isNew(DomainEvent event) {
    String idempotenceKey = idempotenceKey(event);
    boolean isNew = lockService.acquireLock(idempotenceKey);
    if (isNew) {
      log.info("Acquired event idempotence key {}", idempotenceKey);
    }
    return isNew;
  }

  public void deleteKey(DomainEvent event) {
    String idempotenceKey = idempotenceKey(event);
    lockService.releaseLock(idempotenceKey);
    log.info("Deleted event idempotence key {}", idempotenceKey);
  }

  private String idempotenceKey(DomainEvent event) {
    return String.format("event:%s:%s", applicationName, event.getEventId());
  }
}
