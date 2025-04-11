package org.atlas.infrastructure.event.sns.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.json.JsonUtil;
import org.atlas.infrastructure.event.gateway.EventPublisher;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnsEventPublisher implements EventPublisher {

  private final SnsClient snsClient;

  public void publish(DomainEvent event, String snsTopicArn) {
    String message = JsonUtil.getInstance()
        .toJson(event);
    PublishRequest request = PublishRequest.builder()
        .message(message)
        .topicArn(snsTopicArn)
        .build();
    PublishResponse response = snsClient.publish(request);
    log.info("Published event: {}\nStatus: {}. Response: {}",
        event, response.sdkHttpResponse().statusCode(), response);
  }
}
