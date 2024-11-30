package org.atlas.platform.notification.email.core.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class SendEmailRequest {

  private List<String> recipients;
  private String subject;
  private String body;
  private List<Attachment> attachments;
  private boolean html;

  private SendEmailRequest() {
  }

  public static class Builder {

    private List<String> recipients;
    private String subject;
    private String body;
    private List<Attachment> attachments;
    private boolean html;

    public Builder setRecipients(List<String> recipients) {
      this.recipients = recipients;
      return this;
    }

    public Builder addRecipient(String recipient) {
      if (recipients == null) {
        recipients = new ArrayList<>();
      }
      this.recipients.add(recipient);
      return this;
    }

    public Builder setSubject(String subject) {
      this.subject = subject;
      return this;
    }

    public Builder setBody(String body) {
      this.body = body;
      return this;
    }

    public Builder setAttachments(List<Attachment> attachments) {
      this.attachments = attachments;
      return this;
    }

    public Builder addAttachment(Attachment attachment) {
      if (attachments == null) {
        attachments = new ArrayList<>();
      }
      this.attachments.add(attachment);
      return this;
    }

    public Builder setHtml(boolean html) {
      this.html = html;
      return this;
    }

    public SendEmailRequest build() {
      if (!validateRequired()) {
        throw new RuntimeException(
            "Failed to build SendMailRequest, please check the required fields.");
      }
      SendEmailRequest request = new SendEmailRequest();
      request.recipients = this.recipients;
      request.subject = this.subject;
      request.body = this.body;
      request.attachments = this.attachments;
      request.html = this.html;
      return request;
    }

    private boolean validateRequired() {
      return !CollectionUtils.isEmpty(recipients) &&
          StringUtils.hasLength(subject) &&
          StringUtils.hasLength(body);
    }
  }

  public List<String> getRecipients() {
    return recipients;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }

  public boolean isHtml() {
    return html;
  }
}
