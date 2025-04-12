package org.atlas.domain.outbox.entity;

public enum OutboxMessageStatus {

  PENDING,
  PROCESSED,
  FAILED,
}
