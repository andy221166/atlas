package org.atlas.platform.template.core;

import jakarta.annotation.Nonnull;
import java.util.Map;

public interface TemplateResolver {

  String resolve(@Nonnull String templateName) throws Exception;

  String resolve(@Nonnull String templateName, Map<String, Object> data) throws Exception;
}
