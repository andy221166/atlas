package org.atlas.platform.event.core.idempotence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.core.DomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventIdempotenceService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    public boolean isNew(DomainEvent event) {
        String idempotenceKey = idempotenceKey(event);
        // Use Redis' setIfAbsent (equivalent to SETNX) to check and set the event ID atomically
        boolean isNew = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(idempotenceKey, "processed"));
        if (isNew) {
            log.info("Acquired event idempotence key {}", idempotenceKey);
        }
        return isNew;
    }

    public void deleteKey(DomainEvent event) {
        String idempotenceKey = idempotenceKey(event);
        redisTemplate.delete(idempotenceKey);
        log.info("Deleted event idempotence key {}", idempotenceKey);
    }

    private String idempotenceKey(DomainEvent event) {
        return String.format("event:%s:%s", applicationName, event.getEventId());
    }
}
