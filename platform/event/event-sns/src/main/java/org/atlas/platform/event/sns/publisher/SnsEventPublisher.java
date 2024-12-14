package org.atlas.platform.event.sns.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.json.JsonUtil;
import org.atlas.platform.event.contract.DomainEvent;
import org.atlas.platform.event.contract.order.BaseOrderEvent;
import org.atlas.platform.event.core.publisher.EventPublisher;
import org.atlas.platform.event.sns.config.SnsEventProps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnsEventPublisher implements EventPublisher {

    private final SnsEventProps props;
    private final SnsClient snsClient;

    @Override
    public <E extends DomainEvent> void publish(E event) {
        if (event instanceof BaseOrderEvent) {
            if (props.getOrderTopicArn() == null) {
                log.error("No order topic configured");
                return;
            }
            doPublish(event, props.getOrderTopicArn());
        } else {
            throw new IllegalArgumentException("Unsupported event type: " + event.getEventType());
        }
    }

    private void doPublish(DomainEvent event, String topicArn) {
        String message = JsonUtil.toJson(event);
        PublishRequest request = PublishRequest.builder()
            .message(message)
            .topicArn(topicArn)
            .build();
        PublishResponse response = snsClient.publish(request);
        log.info("Published event: {}\nStatus: {}. Response: {}",
            event, response.sdkHttpResponse().statusCode(), response);
    }
}
