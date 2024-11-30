package org.atlas.platform.outbox.processor;

import org.atlas.platform.outbox.model.OutboxMessage;

public interface OutboxMessageProcessor {

  void process(OutboxMessage message) throws Exception;
}
