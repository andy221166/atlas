package org.atlas.service.notification.port.outbound.template;

import jakarta.annotation.Nonnull;
import java.util.Map;

public interface EmailTemplatePort {

  String resolveSubject(@Nonnull String templateName) throws Exception;

  String resolveSubject(@Nonnull String templateName, Map<String, Object> data) throws Exception;

  String resolveBody(@Nonnull String templateName) throws Exception;

  String resolveBody(@Nonnull String templateName, Map<String, Object> data) throws Exception;
}
