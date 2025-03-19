package org.atlas.service.notification.adapter.email.spring.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.service.notification.adapter.email.spring.exception.SendEmailException;
import org.atlas.service.notification.adapter.email.spring.model.Attachment;
import org.atlas.service.notification.adapter.email.spring.model.SendEmailRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender mailSender;

  public void send(SendEmailRequest request) throws SendEmailException {
    try {
      MimeMessage mimeMessage = createMimeMessage(request);
      mailSender.send(mimeMessage);
      log.info("Sent email successfully: recipients={}", request.getRecipients());
    } catch (MessagingException e) {
      log.error("Failed to send email: recipients={}", request.getRecipients(), e);
      throw new SendEmailException(e);
    }
  }

  private MimeMessage createMimeMessage(SendEmailRequest request) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,
        StandardCharsets.UTF_8.name());
    helper.setFrom(request.getSender());
    helper.setTo(request.getRecipients().toArray(new String[0]));
    helper.setSubject(request.getSubject());
    helper.setText(request.getBody(), request.isHtml());
    if (CollectionUtils.isNotEmpty(request.getAttachments())) {
      for (Attachment attachment : request.getAttachments()) {
        helper.addAttachment(attachment.fileName(), attachment.file());
      }
    }
    return mimeMessage;
  }
}
