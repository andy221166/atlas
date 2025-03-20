package org.atlas.service.notification.application.event;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.util.FileUtil;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.service.notification.application.config.EmailProps;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.atlas.service.notification.port.outbound.email.Attachment;
import org.atlas.service.notification.port.outbound.email.EmailPort;
import org.atlas.service.notification.port.outbound.email.SendEmailRequest;
import org.atlas.service.notification.port.outbound.sse.SsePort;
import org.atlas.service.notification.port.outbound.template.EmailTemplatePort;
import org.atlas.service.notification.port.outbound.websocket.OrderWebSocketPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConfirmedEventHandlerImpl implements OrderConfirmedEventHandler {

  private final EmailPort emailPort;
  private final EmailProps emailProps;
  private final EmailTemplatePort emailTemplatePort;
  private final SsePort ssePort;
  private final OrderWebSocketPort orderWebSocketPort;

  @Override
  public void handle(OrderConfirmedEvent event) {
    notifyEmail(event);
    notifySse(event);
    notifyWebSocket(event);
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
      throw new RuntimeException("Could not resolve subject template", e);
    }

    // Body
    String body;
    try {
      body = emailTemplatePort.resolveBody("order_confirmed", model);
    } catch (Exception e) {
      throw new RuntimeException("Could not resolve body template", e);
    }

    // Attachments (demo)
    Attachment attachment;
    File attachmentFile;
    try {
      attachmentFile = FileUtil.readResourceFile("email/attachment/coffee.jpg");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    attachment = new Attachment("email/attachment/coffee.jpg", attachmentFile);

    SendEmailRequest request = new SendEmailRequest.Builder()
        .setSender(emailProps.getSender())
        .addRecipient(event.getUser().getEmail())
        .setSubject(subject)
        .setBody(body)
        .addAttachment(attachment)
        .setHtml(true)
        .build();
    emailPort.send(request);
    log.info("Sent email: event={}", event);
  }

  private void notifySse(OrderConfirmedEvent event) {
    log.info("Notifying OrderConfirmedEvent: orderId={}", event.getOrderId());

  }

  private void notifyWebSocket(OrderConfirmedEvent event) {

  }
}
