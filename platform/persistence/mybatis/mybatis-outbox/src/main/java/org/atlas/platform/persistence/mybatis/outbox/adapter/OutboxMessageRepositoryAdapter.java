package org.atlas.platform.persistence.mybatis.outbox.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;
import org.atlas.platform.outbox.repository.OutboxMessageRepository;
import org.atlas.platform.persistence.mybatis.outbox.mapper.OutboxMessageMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMessageRepositoryAdapter implements OutboxMessageRepository {

  private final OutboxMessageMapper outboxMessageMapper;

  @Override
  public List<OutboxMessage> findByStatus(OutboxMessageStatus status) {
    return outboxMessageMapper.findByStatus(status);
  }

  @Override
  public void insert(OutboxMessage outboxMessage) {
    outboxMessageMapper.insert(outboxMessage);
  }

  @Override
  public void update(OutboxMessage outboxMessage) {
    outboxMessageMapper.update(outboxMessage);
  }
}
