package org.atlas.platform.notification.email.core.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendEmailRequest {

  private String source;
  private List<String> destinations;
  private String subject;
  private String body;
  private List<Attachment> attachments;
  private boolean html;

  public static class Builder {

    private String source;
    private List<String> destinations;
    private String subject;
    private String body;
    private List<Attachment> attachments;
    private boolean html;

    public Builder setSource(String source) {
      this.source = source;
      return this;
    }

    public Builder setDestinations(List<String> destinations) {
      this.destinations = destinations;
      return this;
    }

    public Builder addDestination(String destination) {
      if (destinations == null) {
        destinations = new ArrayList<>();
      }
      this.destinations.add(destination);
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
      this.source = request.getSource();
      request.destinations = this.destinations;
      request.subject = this.subject;
      request.body = this.body;
      request.attachments = this.attachments;
      request.html = this.html;
      return request;
    }

    private boolean validateRequired() {
      return StringUtils.isNotBlank(this.source) &&
          CollectionUtils.isNotEmpty(destinations) &&
          StringUtils.isNotBlank(subject) &&
          StringUtils.isNotBlank(body);
    }
  }
}
