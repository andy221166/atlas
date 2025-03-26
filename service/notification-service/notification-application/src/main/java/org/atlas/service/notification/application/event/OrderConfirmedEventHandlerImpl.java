package org.atlas.service.notification.application.event;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.util.FileUtil;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.service.notification.application.config.EmailProps;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.atlas.service.notification.port.outbound.email.Attachment;
import org.atlas.service.notification.port.outbound.email.EmailNotification;
import org.atlas.service.notification.port.outbound.email.EmailPort;
import org.atlas.service.notification.port.outbound.realtime.enums.RealtimeNotificationType;
import org.atlas.service.notification.port.outbound.realtime.model.OrderStatusChangedPayload;
import org.atlas.service.notification.port.outbound.realtime.sse.SseNotification;
import org.atlas.service.notification.port.outbound.realtime.sse.SsePort;
import org.atlas.service.notification.port.outbound.realtime.websocket.WebSocketNotification;
import org.atlas.service.notification.port.outbound.realtime.websocket.WebSocketPort;
import org.atlas.service.notification.port.outbound.template.EmailTemplatePort;
import org.atlas.service.notification.port.outbound.template.ResolveTemplateException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConfirmedEventHandlerImpl implements OrderConfirmedEventHandler {

  private final EmailPort emailPort;
  private final EmailProps emailProps;
  private final EmailTemplatePort emailTemplatePort;
  private final SsePort<Integer> ssePort;
  private final WebSocketPort webSocketPort;

  @Override
  public void handle(OrderConfirmedEvent event) {
    CompletableFuture.allOf(
        CompletableFuture.runAsync(() -> notifyEmail(event))
            .exceptionally(e -> {
              log.error("Failed to notify email for event {}", event.getEventId(), e);
              return null;
            }),
        CompletableFuture.runAsync(() -> notifySse(event))
            .exceptionally(e -> {
              log.error("Failed to notify SSE for event {}", event.getEventId(), e);
              return null;
            }),
        CompletableFuture.runAsync(() -> notifyWebSocket(event))
            .exceptionally(e -> {
              log.error("Failed to notify WebSocket for event {}", event.getEventId(), e);
              return null;
            })
    ).join();
  }

  private void notifyEmail(OrderConfirmedEvent event) {
    // Model
    Map<String, Object> model = new HashMap<>();
    model.put("order", event);

    // Subject
    String subject;
    try {
      subject = emailTemplatePort.resolveSubject("order_confirmed", model);
    } catch (Exception e) {
      throw new ResolveTemplateException("Could not resolve subject template", e);
    }

    // Body
    String body;
    try {
      body = emailTemplatePort.resolveBody("order_confirmed", model);
    } catch (Exception e) {
      throw new ResolveTemplateException("Could not resolve body template", e);
    }

    // Attachments (demo)
    Attachment attachment;
    File attachmentFile;
    try {
      attachmentFile = FileUtil.readResourceFile("email/attachment/coffee.jpg");
    } catch (IOException e) {
      throw new ResolveTemplateException("Could not resolve attachment", e);
    }
    attachment = new Attachment("email/attachment/coffee.jpg", attachmentFile);

    EmailNotification notification = new EmailNotification.Builder()
        .setSender(emailProps.getSender())
        .addRecipient(event.getUser().getEmail())
        .setSubject(subject)
        .setBody(body)
        .addAttachment(attachment)
        .setHtml(true)
        .build();
    emailPort.notify(notification);
    log.info("Sent email: event={}", event);
  }

  private void notifySse(OrderConfirmedEvent event) {
    OrderStatusChangedPayload payload = OrderStatusChangedPayload.from(event);
    SseNotification<Integer> notification = new SseNotification<>(
        RealtimeNotificationType.ORDER_STATUS_CHANGED, event.getOrderId(), payload);
    ssePort.notify(notification);
  }

  private void notifyWebSocket(OrderConfirmedEvent event) {
    OrderStatusChangedPayload payload = OrderStatusChangedPayload.from(event);
    WebSocketNotification notification = new WebSocketNotification(
        RealtimeNotificationType.ORDER_STATUS_CHANGED, payload);
    webSocketPort.notify(notification);
  }
}
