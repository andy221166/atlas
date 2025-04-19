package org.atlas.framework.observability.metrics;

public interface ApiLatencyPublisher extends MetricPublisher {

  void publish(String service, String endpoint, String method, int httpStatus, String channel, long elapsedTimeMs);

  @Override
  default String metricName() {
    return "api.latency.ms";
  }
}
