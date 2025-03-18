package org.atlas.service.notification.application.event.email;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.util.FileUtil;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.notification.email.core.config.EmailProps;
import org.atlas.platform.notification.email.core.model.Attachment;
import org.atlas.platform.notification.email.core.model.SendEmailRequest;
import org.atlas.platform.notification.email.core.service.EmailService;
import org.atlas.platform.template.contract.TemplateResolver;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailOrderConfirmedEventHandlerImpl implements OrderConfirmedEventHandler {

  private static final String TEMPLATE_SUBJECT_DIR = "email/subject/";
  private static final String TEMPLATE_BODY_DIR = "email/body/";

  private final TemplateResolver templateResolver;
  private final EmailService emailService;
  private final EmailProps emailProps;

  @Async
  @Override
  public void handle(OrderConfirmedEvent event) {
    // Model
    Map<String, Object> model = new HashMap<>();
    model.put("order", event);

    // Subject
    String subject;
    try {
      subject = templateResolver.resolve(TEMPLATE_SUBJECT_DIR + "order_confirmed", model);
    } catch (Exception e) {
      throw new RuntimeException("Could not resolve subject template", e);
    }

    // Body
    String body;
    try {
      body = templateResolver.resolve(TEMPLATE_BODY_DIR + "order_confirmed", model);
    } catch (Exception e) {
      throw new RuntimeException("Could not resolve body template", e);
    }

    // Attachments (demo)
    Attachment attachment;
    File attachmentFile;
    try {
      attachmentFile = FileUtil.readResourceFile("coffee.jpg");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    attachment = new Attachment("coffee.jpg", attachmentFile);

    SendEmailRequest request = new SendEmailRequest.Builder()
        .setSource(emailProps.getSource())
        .addDestination(event.getUser().getEmail())
        .setSubject(subject)
        .setBody(body)
        .addAttachment(attachment)
        .setHtml(true)
        .build();
    emailService.send(request);
    log.info("Sent email: event={}", event);
  }
}
