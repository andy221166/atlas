package org.atlas.service.notification.adapter.email.ses.adapter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.util.FileUtil;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.template.core.TemplateResolver;
import org.atlas.service.notification.adapter.email.ses.config.Constant;
import org.atlas.service.notification.adapter.email.ses.config.EmailProps;
import org.atlas.service.notification.adapter.email.ses.exception.SendEmailException;
import org.atlas.service.notification.adapter.email.ses.model.Attachment;
import org.atlas.service.notification.adapter.email.ses.model.SendEmailRequest;
import org.atlas.service.notification.adapter.email.ses.service.EmailService;
import org.atlas.service.notification.port.outbound.email.OrderEmailPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEmailAdapter implements OrderEmailPort {

  private final EmailService emailService;
  private final TemplateResolver templateResolver;
  private final EmailProps emailProps;

  @Override
  public void notify(OrderConfirmedEvent event) throws SendEmailException {
    // Model
    Map<String, Object> model = new HashMap<>();
    model.put("order", event);

    // Subject
    String subject;
    try {
      subject = templateResolver.resolve(Constant.TEMPLATE_SUBJECT_DIR + "/order_confirmed", model);
    } catch (Exception e) {
      throw new RuntimeException("Could not resolve subject template", e);
    }

    // Body
    String body;
    try {
      body = templateResolver.resolve(Constant.TEMPLATE_BODY_DIR + "/order_confirmed", model);
    } catch (Exception e) {
      throw new RuntimeException("Could not resolve body template", e);
    }

    // Attachments (demo)
    Attachment attachment;
    File attachmentFile;
    try {
      attachmentFile = FileUtil.readResourceFile("templates/attachment/coffee.jpg");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    attachment = new Attachment("templates/attachment/coffee.jpg", attachmentFile);

    SendEmailRequest request = new SendEmailRequest.Builder()
        .setSender(emailProps.getSender())
        .addRecipient(event.getUser().getEmail())
        .setSubject(subject)
        .setBody(body)
        .addAttachment(attachment)
        .setHtml(true)
        .build();
    emailService.send(request);
    log.info("Sent email: event={}", event);
  }
}
