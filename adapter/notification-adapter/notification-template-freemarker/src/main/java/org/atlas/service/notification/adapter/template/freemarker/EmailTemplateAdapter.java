package org.atlas.service.notification.adapter.template.freemarker;

import jakarta.annotation.Nonnull;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.atlas.port.outbound.notification.template.EmailTemplatePort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailTemplateAdapter implements EmailTemplatePort {

  public static final String TEMPLATE_SUBJECT_DIR = "email/subject";
  public static final String TEMPLATE_BODY_DIR = "email/body";

  private final FreemarkerTemplateResolver freemarkerTemplateResolver;

  @Override
  public String resolveSubject(@Nonnull String templateName) throws Exception {
    return resolveSubject(templateName, Map.of());
  }

  @Override
  public String resolveSubject(@Nonnull String templateName, Map<String, Object> data)
      throws Exception {
    String path = TEMPLATE_SUBJECT_DIR + "/" + templateName + ".ftl";
    return freemarkerTemplateResolver.resolve(path, data);
  }

  @Override
  public String resolveBody(@Nonnull String templateName) throws Exception {
    return resolveBody(templateName, Map.of());
  }

  @Override
  public String resolveBody(@Nonnull String templateName, Map<String, Object> data)
      throws Exception {
    String path = TEMPLATE_BODY_DIR + "/" + templateName + ".ftl";
    return freemarkerTemplateResolver.resolve(path, data);
  }
}
