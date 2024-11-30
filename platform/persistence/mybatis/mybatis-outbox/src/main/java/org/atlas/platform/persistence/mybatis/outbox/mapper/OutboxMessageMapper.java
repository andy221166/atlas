package org.atlas.platform.persistence.mybatis.outbox.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;

@Mapper
public interface OutboxMessageMapper {

  List<OutboxMessage> findByStatus(@Param("status") OutboxMessageStatus status);

  int insert(OutboxMessage outboxMessage);

  int update(OutboxMessage outboxMessage);
}
