package org.atlas.service.user.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Preference extends Auditable implements Serializable {

  private Integer id;
  private String language;
  private String timezone;
}
