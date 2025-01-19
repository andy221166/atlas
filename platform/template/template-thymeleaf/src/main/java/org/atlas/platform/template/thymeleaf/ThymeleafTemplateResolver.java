package org.atlas.platform.template.thymeleaf;

import java.util.Map;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.template.contract.TemplateResolver;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class ThymeleafTemplateResolver implements TemplateResolver {

  private final TemplateEngine templateEngine;

  @Override
  public String resolve(@Nonnull String templateName) throws Exception {
    return resolve(templateName, null);
  }

  @Override
  public String resolve(@Nonnull String templateName, Map<String, Object> data)
      throws Exception {
    // Create a context with the provided data
    Context context = new Context();
    // Add each entry in the data map to the context
    if (data != null) {
      data.forEach(context::setVariable);
    }
    // Render the template with the provided data
    return templateEngine.process(templateName, context);
  }
}
