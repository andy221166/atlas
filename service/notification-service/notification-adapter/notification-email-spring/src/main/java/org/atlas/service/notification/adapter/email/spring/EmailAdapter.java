package org.atlas.service.notification.adapter.email.spring;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.service.notification.port.outbound.email.Attachment;
import org.atlas.service.notification.port.outbound.email.EmailNotification;
import org.atlas.service.notification.port.outbound.email.EmailPort;
import org.atlas.service.notification.port.outbound.email.SendEmailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Email")
public class EmailAdapter implements EmailPort {

  private final JavaMailSender mailSender;

  @Override
  public void notify(EmailNotification notification) throws SendEmailException {
    try {
      MimeMessage mimeMessage = createMimeMessage(notification);
      mailSender.send(mimeMessage);
      log.info("Sent email successfully: recipients={}", notification.getRecipients());
    } catch (MessagingException e) {
      log.error("Failed to send email: recipients={}", notification.getRecipients(), e);
      throw new SendEmailException(e);
    }
  }

  private MimeMessage createMimeMessage(EmailNotification request) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,
        StandardCharsets.UTF_8.name());
    helper.setFrom(request.getSender());
    helper.setTo(request.getRecipients().toArray(new String[0]));
    helper.setSubject(request.getSubject());
    helper.setText(request.getBody(), request.isHtml());
    if (CollectionUtils.isNotEmpty(request.getAttachments())) {
      for (Attachment attachment : request.getAttachments()) {
        helper.addAttachment(attachment.file().getName(), attachment.file());
      }
    }
    return mimeMessage;
  }
}
