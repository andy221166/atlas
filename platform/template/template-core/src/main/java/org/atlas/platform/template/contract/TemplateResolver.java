package org.atlas.platform.template.contract;

import javax.annotation.Nonnull;
import java.util.Map;

public interface TemplateResolver {

    String resolve(@Nonnull String templateName) throws Exception;

    String resolve(@Nonnull String templateName, @Nonnull Map<String, Object> data) throws Exception;
}
