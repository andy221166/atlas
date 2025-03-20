package org.atlas.platform.event.sns;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.gateway.EventPublisher;
import org.atlas.platform.json.JsonUtil;
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
    String message = JsonUtil.getInstance().toJson(event);
    PublishRequest request = PublishRequest.builder()
        .message(message)
        .topicArn(snsTopicArn)
        .build();
    PublishResponse response = snsClient.publish(request);
    log.info("Published event: {}\nStatus: {}. Response: {}",
        event, response.sdkHttpResponse().statusCode(), response);
  }
}
