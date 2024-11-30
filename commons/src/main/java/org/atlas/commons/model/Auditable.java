package org.atlas.commons.model;

import java.util.Date;
import lombok.Data;

@Data
public class Auditable {

  protected Date createdAt;
  protected Date updatedAt;
}
