package org.atlas.infrastructure.transaction;

import org.atlas.framework.transaction.TransactionPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TransactionAdapter implements TransactionPort {

  private final PlatformTransactionManager platformTransactionManager;
  private final ThreadLocal<TransactionStatus> transactionStatusThreadLocal = new ThreadLocal<>();

  @Autowired
  public TransactionAdapter(PlatformTransactionManager platformTransactionManager) {
    this.platformTransactionManager = platformTransactionManager;
  }

  @Override
  public void begin() {
    TransactionStatus transactionStatus = platformTransactionManager.getTransaction(
        new DefaultTransactionDefinition());
    transactionStatusThreadLocal.set(transactionStatus);
  }

  @Override
  public void commit() {
    TransactionStatus status = transactionStatusThreadLocal.get();
    if (status != null && !status.isCompleted()) {
      platformTransactionManager.commit(status);
      transactionStatusThreadLocal.remove();
    }
  }

  @Override
  public void rollback() {
    TransactionStatus status = transactionStatusThreadLocal.get();
    if (status != null && !status.isCompleted()) {
      platformTransactionManager.rollback(status);
      transactionStatusThreadLocal.remove();
    }
  }
}
