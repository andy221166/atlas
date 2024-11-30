package org.atlas.platform.event.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SagaMode {

  public static final String ORCHESTRATION = "orchestration";
  public static final String CHOREOGRAPHY = "choreography";
}
