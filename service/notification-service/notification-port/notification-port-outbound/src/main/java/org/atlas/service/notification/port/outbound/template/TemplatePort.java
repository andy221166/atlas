package org.atlas.service.notification.port.outbound.template;

import jakarta.annotation.Nonnull;
import java.util.Map;

public interface TemplatePort {

  String resolve(@Nonnull String templateName) throws Exception;

  String resolve(@Nonnull String templateName, Map<String, Object> data) throws Exception;
}
