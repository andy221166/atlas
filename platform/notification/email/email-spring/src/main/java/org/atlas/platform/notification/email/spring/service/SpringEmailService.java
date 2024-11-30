package org.atlas.platform.notification.email.spring.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.notification.email.core.exception.SendEmailException;
import org.atlas.platform.notification.email.core.model.Attachment;
import org.atlas.platform.notification.email.core.model.SendEmailRequest;
import org.atlas.platform.notification.email.core.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SpringEmailService implements EmailService {

  private final JavaMailSender mailSender;

  public SpringEmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void send(SendEmailRequest request) throws SendEmailException {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true,
          StandardCharsets.UTF_8.name());
      helper.setTo(request.getRecipients().toArray(new String[0]));
      helper.setSubject(request.getSubject());
      helper.setText(request.getBody(), request.isHtml());
      if (CollectionUtils.isNotEmpty(request.getAttachments())) {
        for (Attachment attachment : request.getAttachments()) {
          helper.addAttachment(attachment.getFileName(), attachment.getFile());
        }
      }
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new SendEmailException(e);
    }
  }
}
