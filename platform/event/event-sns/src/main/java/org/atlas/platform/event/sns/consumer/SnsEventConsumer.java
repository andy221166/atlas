package org.atlas.platform.event.sns.consumer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.atlas.commons.util.ConcurrentUtil;
import org.atlas.commons.util.json.JsonUtil;
import org.atlas.platform.event.gateway.consumer.EventDispatcher;
import org.atlas.platform.event.gateway.model.DomainEvent;
import org.atlas.platform.event.sns.config.SnsEventProps;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnsEventConsumer {

  private final SnsEventProps props;
  private final SqsClient sqsClient;
  private final EventDispatcher eventDispatcher;
  private final AtomicBoolean isRunning = new AtomicBoolean(true);
  private ExecutorService executorService;

  @PostConstruct
  public void init() {
    if (StringUtils.isNotBlank(props.getOrderQueueUrl())) {
      consumeMessages("order_events", props.getOrderQueueUrl());
    }
  }

  @PreDestroy
  public void shutdown() {
    log.info("Shutting down SQS consumer");
    isRunning.set(false);
    ConcurrentUtil.shutdown(executorService);
  }

  private void consumeMessages(String queueName, String queueUrl) {
    executorService = Executors.newSingleThreadExecutor(r -> {
      Thread thread = new Thread(r, queueName);
      thread.setDaemon(true);
      return thread;
    });

    CompletableFuture.runAsync(() -> {
          log.info("Starting SQS consumer for queue: {}", queueUrl);

          ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
              .queueUrl(queueUrl)
              .maxNumberOfMessages(props.getBatchSize())
              .waitTimeSeconds(props.getWaitTimeSeconds())
              .visibilityTimeout(props.getVisibilityTimeout())
              .build();

          while (isRunning.get()) {
            try {
              List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
              if (CollectionUtils.isNotEmpty(messages)) {
                messages.forEach(message -> {
                  log.debug("Handling message: {}", message.messageId());
                  try {
                    handleMessage(message);
                    deleteMessage(message, queueUrl);
                  } catch (Exception e) {
                    log.error("Failed to handle message: messageId={}", message.messageId(), e);
                  }
                });
              }
            } catch (Exception e) {
              log.error("Failed to receive messages from SQS queue", e);
            }
          }
        }, executorService)
        .exceptionally(throwable -> {
          log.error("Fatal error in SQS consumer", throwable);
          return null;
        });
  }

  private void handleMessage(Message message) {
    // Note: Need to enable raw message delivery when create subscription to be able to get message body directly
    DomainEvent event = JsonUtil.toObject(message.body(), DomainEvent.class);
    eventDispatcher.dispatch(event);
  }

  private void deleteMessage(Message message, String queueUrl) {
    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
        .queueUrl(queueUrl)
        .receiptHandle(message.receiptHandle())
        .build();
    sqsClient.deleteMessage(deleteMessageRequest);
    log.info("Deleted message: messageId={}", message.messageId());
  }
}
