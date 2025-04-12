package org.atlas.infrastructure.persistence.jpa.adapter.outbox;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.outbox.entity.OutboxMessageEntity;
import org.atlas.domain.outbox.entity.OutboxMessageStatus;
import org.atlas.domain.outbox.repository.OutboxMessageRepository;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.persistence.jpa.adapter.outbox.entity.JpaOutboxMessageEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.outbox.repository.JpaOutboxMessageRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMessageRepositoryAdapter implements OutboxMessageRepository {

  private final JpaOutboxMessageRepository jpaOutboxMessageRepository;

  @Override
  public List<OutboxMessageEntity> findByStatusOrderByCreatedAt(OutboxMessageStatus status) {
    List<JpaOutboxMessageEntity> jpaOutboxMessageEntities =
        jpaOutboxMessageRepository.findByStatusOrderByCreatedAt(status);
    return ObjectMapperUtil.getInstance()
        .mapList(jpaOutboxMessageEntities, OutboxMessageEntity.class);
  }

  @Override
  public void insert(OutboxMessageEntity outboxMessageEntity) {
    JpaOutboxMessageEntity jpaOutboxMessageEntity = ObjectMapperUtil.getInstance()
        .map(outboxMessageEntity, JpaOutboxMessageEntity.class);
    jpaOutboxMessageRepository.save(jpaOutboxMessageEntity);
    outboxMessageEntity.setId(jpaOutboxMessageEntity.getId());
  }

  @Override
  public void update(OutboxMessageEntity outboxMessageEntity) {
    JpaOutboxMessageEntity jpaOutboxMessageEntity = ObjectMapperUtil.getInstance()
        .map(outboxMessageEntity, JpaOutboxMessageEntity.class);
    jpaOutboxMessageRepository.save(jpaOutboxMessageEntity);
  }
}
