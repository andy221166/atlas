package org.atlas.platform.commons.model;

import java.util.Date;
import lombok.Data;

@Data
public class AuditableEntity {

  protected Date createdAt;

  protected Date updatedAt;
}
