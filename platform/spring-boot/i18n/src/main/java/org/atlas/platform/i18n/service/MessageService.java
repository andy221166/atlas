package org.atlas.platform.i18n.service;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.util.StringUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final Locale locale;
  private final MessageSource messageSource;

  public String getMessage(String code, Object... params) {
    return getMessage(code, StringUtil.EMPTY, params);
  }

  public String getMessage(String code, String defaultValue, Object... params) {
    try {
      return messageSource.getMessage(code, params, locale);
    } catch (NoSuchMessageException e) {
      return defaultValue;
    }
  }
}
