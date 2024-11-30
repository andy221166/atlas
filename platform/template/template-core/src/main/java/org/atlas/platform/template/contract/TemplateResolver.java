package org.atlas.platform.template.contract;

import java.util.Map;
import javax.annotation.Nonnull;

public interface TemplateResolver {

  String resolve(@Nonnull String templateName) throws Exception;

  String resolve(@Nonnull String templateName, @Nonnull Map<String, Object> data) throws Exception;
}
