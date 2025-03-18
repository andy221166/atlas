package org.atlas.platform.event.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaEventPublisherConfig {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public ProducerFactory<String, Object> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();

    // Basic configuration
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    // Reliability settings
    configProps.put(ProducerConfig.ACKS_CONFIG, "all");  // Strongest durability guarantee
    configProps.put(ProducerConfig.RETRIES_CONFIG, 3);   // Number of retries if the request fails
    configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // Delay between retries

    // Performance tuning
    configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);  // Batch size in bytes
    configProps.put(ProducerConfig.LINGER_MS_CONFIG, 10);      // Wait up to 10ms to batch messages
    configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 32MB producer buffer

    // Compression
    configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

    // Idempotence settings (exactly-once semantics)
    configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
