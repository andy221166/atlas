package org.atlas.platform.orm.mybatis.outbox.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.platform.outbox.model.OutboxMessage;
import org.atlas.platform.outbox.model.OutboxMessageStatus;

@Mapper
public interface OutboxMessageMapper {

  OutboxMessage findById(@Param("id") Long id);

  List<OutboxMessage> findByStatus(@Param("status") OutboxMessageStatus status);

  int insert(OutboxMessage outboxMessage);

  int update(OutboxMessage outboxMessage);
}
