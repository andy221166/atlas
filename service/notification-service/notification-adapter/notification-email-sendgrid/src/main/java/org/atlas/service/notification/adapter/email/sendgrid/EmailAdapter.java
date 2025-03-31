package org.atlas.service.notification.adapter.email.sendgrid;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.cryptography.Base64Util;
import org.atlas.service.notification.port.outbound.email.Attachment;
import org.atlas.service.notification.port.outbound.email.EmailNotification;
import org.atlas.service.notification.port.outbound.email.EmailPort;
import org.atlas.service.notification.port.outbound.email.SendEmailException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Email")
public class EmailAdapter implements EmailPort {

  // Spring Boot automatically configures SendGrid bean
  private final SendGrid sendGrid;

  @Override
  public void notify(EmailNotification notification) throws SendEmailException {
    try {
      Request request = createSendGridRequest(notification);
      Response response = sendGrid.api(request);
      if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
        log.info("Sent email successfully: notificationId={}, recipients={}",
            notification.getId(), notification.getRecipients());
      } else {
        log.error("Failed to send email: notificationId={}, recipients={}, status={}, body={}",
            notification.getId(), notification.getRecipients(), response.getStatusCode(), response.getBody());
        throw new SendEmailException("SendGrid API error: " + response.getBody());
      }
    } catch (IOException e) {
      log.error("Failed to send email: notificationId={}, recipients={}",
          notification.getId(), notification.getRecipients(), e);
      throw new SendEmailException(e);
    }
  }

  private Request createSendGridRequest(EmailNotification notification) throws IOException {
    Email from = new Email(notification.getSender());
    Email[] recipients = notification.getRecipients().stream().map(Email::new)
        .toArray(Email[]::new);
    Content content = new Content(notification.isHtml() ? "text/html" : "text/plain",
        notification.getBody());
    Mail mail = new Mail();
    mail.setFrom(from);
    mail.setSubject(notification.getSubject());
    Personalization personalization = new Personalization();
    for (Email recipient : recipients) {
      personalization.addTo(recipient);
    }
    mail.addPersonalization(personalization);
    mail.addContent(content);

    if (CollectionUtils.isNotEmpty(notification.getAttachments())) {
      for (Attachment attachment : notification.getAttachments()) {
        Attachments sendGridAttachment = new Attachments();
        sendGridAttachment.setFilename(attachment.name());
        sendGridAttachment.setContent(Base64Util.encode(attachment.content()));
        mail.addAttachments(sendGridAttachment);
      }
    }

    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    return request;
  }
}
