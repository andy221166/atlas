package org.atlas.service.aggregator.domain;

public enum Aggregator {

  API, // Fetch data via API directly
  EVENT, // Event-driven architecture
  CDC, // Change data capture
  JOB, // Batch job
}
