package org.atlas.service.user.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Organization extends Auditable implements Serializable {

  @EqualsAndHashCode.Include
  private Integer id;
  private String name;
}
