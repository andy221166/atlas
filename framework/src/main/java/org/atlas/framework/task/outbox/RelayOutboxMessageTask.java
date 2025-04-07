package org.atlas.framework.task.outbox;

public interface RelayOutboxMessageTask {

  void execute() throws Exception;
}
