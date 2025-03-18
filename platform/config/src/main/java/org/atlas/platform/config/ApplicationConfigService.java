package org.atlas.platform.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ApplicationConfigService {

  @Value("${spring.application.name}")
  private String applicationName;
}
