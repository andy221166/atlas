package org.atlas.infrastructure.event.gateway.outbox.entity;

public enum OutboxMessageStatus {

  PENDING,
  PROCESSED,
  FAILED,
}
