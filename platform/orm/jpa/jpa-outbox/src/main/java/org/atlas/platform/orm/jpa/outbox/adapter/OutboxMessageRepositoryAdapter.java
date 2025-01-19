package org.atlas.platform.orm.jpa.outbox.adapter;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperAdapter;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.outbox.repository.OutboxMessageRepository;
import org.atlas.platform.orm.jpa.outbox.entity.JpaOutboxMessage;
import org.atlas.platform.orm.jpa.outbox.repository.JpaOutboxMessageRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMessageRepositoryAdapter implements OutboxMessageRepository {

  private final JpaOutboxMessageRepository jpaOutboxMessageRepository;

  @Override
  public Optional<OutboxMessage> findById(Long id) {
    return jpaOutboxMessageRepository.findById(id)
        .map(jpaOutboxMessage -> ModelMapperAdapter.map(jpaOutboxMessage, OutboxMessage.class));
  }

  @Override
  public List<OutboxMessage> findByStatus(OutboxMessageStatus status) {
    List<JpaOutboxMessage> jpaOutboxMessages = jpaOutboxMessageRepository.findByStatus(
        OutboxMessageStatus.PENDING);
    return jpaOutboxMessages.stream()
        .map(jpaOutboxMessage -> ModelMapperAdapter.map(jpaOutboxMessage, OutboxMessage.class))
        .toList();
  }

  @Override
  public void insert(OutboxMessage outboxMessage) {
    JpaOutboxMessage jpaOutboxMessage = ModelMapperAdapter.map(outboxMessage, JpaOutboxMessage.class);
    jpaOutboxMessageRepository.insert(jpaOutboxMessage);
    outboxMessage.setId(jpaOutboxMessage.getId());
  }

  @Override
  public void update(OutboxMessage outboxMessage) {
    JpaOutboxMessage jpaOutboxMessage = ModelMapperAdapter.map(outboxMessage, JpaOutboxMessage.class);
    jpaOutboxMessageRepository.save(jpaOutboxMessage);
  }
}
