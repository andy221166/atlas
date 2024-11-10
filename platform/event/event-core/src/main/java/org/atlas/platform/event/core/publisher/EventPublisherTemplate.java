package org.atlas.platform.event.core.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.json.JsonUtil;
import org.atlas.platform.event.core.DomainEvent;
import org.atlas.platform.event.core.outbox.OutboxMessage;
import org.atlas.platform.event.core.outbox.OutboxMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisherTemplate {

    @Value("${app.outbox.enabled:true}")
    private boolean outboxEnabled;

    private final OutboxMessageRepository outboxMessageRepository;
    private final TransactionTemplate transactionTemplate;
    private final EventPublisher eventPublisher;

    @Transactional
    public <E extends DomainEvent> void publish(E event) {
        if (!outboxEnabled) {
            eventPublisher.publish(event);
            return;
        }

        String eventJson = JsonUtil.toJson(event);
        OutboxMessage message = new OutboxMessage(eventJson);
        outboxMessageRepository.save(message);

        // Create new transaction to isolate this action.
        // If occurs exception, a scheduled job will recover it later.
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            eventPublisher.publish(event);
            outboxMessageRepository.deleteById(message.getId());
            log.info("Published outbox message: {}", message);
        });
    }
}
