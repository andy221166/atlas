package org.atlas.infrastructure.event.sns.adapter.auth.consumer;

import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.auth.event.UserRegisteredEventHandler;
import org.atlas.framework.event.DomainEvent;
import org.atlas.framework.event.contract.user.UserRegisteredEvent;
import org.atlas.infrastructure.event.sns.core.SnsEventConsumer;
import org.atlas.infrastructure.event.sns.core.SnsEventProps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@Slf4j
public class UserRegisteredEventConsumer extends SnsEventConsumer implements InitializingBean {

  private final UserRegisteredEventHandler userRegisteredEventHandler;

  public UserRegisteredEventConsumer(SnsEventProps snsEventProps,
      SqsClient sqsClient,
      UserRegisteredEventHandler userRegisteredEventHandler) {
    super(snsEventProps, sqsClient);
    this.userRegisteredEventHandler = userRegisteredEventHandler;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    consumeMessages("user-registered-event",
        snsEventProps.getSqsQueueUrl().getUserRegisteredEvent());
  }

  @Override
  protected void handleEvent(DomainEvent event) {
    userRegisteredEventHandler.handle((UserRegisteredEvent) event);
  }
}
