package org.atlas.platform.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.template.contract.TemplateResolver;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Component
@RequiredArgsConstructor
public class FreemarkerTemplateResolver implements TemplateResolver {

  private final Configuration configuration;

  @Override
  public String resolve(@Nonnull String templateName) throws TemplateException, IOException {
    return resolve(templateName, new HashMap<>());
  }

  /**
   * @param templateName The relative path of template file in resources/templates folder.
   */
  @Override
  public String resolve(@Nonnull String templateName, @Nonnull Map<String, Object> data)
      throws IOException, TemplateException {
    return FreeMarkerTemplateUtils.processTemplateIntoString(
        configuration.getTemplate(templateName), data);
  }
}
