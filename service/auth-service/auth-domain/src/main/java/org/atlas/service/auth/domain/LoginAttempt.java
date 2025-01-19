package org.atlas.service.auth.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class LoginAttempt extends Auditable implements Serializable {

  private Integer id;
  private String username;
  private String ipAddress;
  private Boolean successful;
  private String failureReason;
}
