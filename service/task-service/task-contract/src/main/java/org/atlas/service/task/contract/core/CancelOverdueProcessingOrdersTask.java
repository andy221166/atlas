package org.atlas.service.task.contract.core;

public interface CancelOverdueProcessingOrdersTask extends BaseTask {

  String CANCELED_REASON = "Overdue pending order";
}
