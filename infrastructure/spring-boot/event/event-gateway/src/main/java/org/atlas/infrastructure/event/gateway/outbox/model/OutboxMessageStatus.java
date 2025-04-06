package org.atlas.infrastructure.event.gateway.outbox.model;

public enum OutboxMessageStatus {

  PENDING,
  PROCESSED,
  FAILED,
}
