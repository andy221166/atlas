package org.atlas.service.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.commons.model.Auditable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class ImportUserRequest extends Auditable {

  @EqualsAndHashCode.Include
  private Integer id;

  private ImportUserRequestStatus status;

  // Object key of temporary file
  private String objectKey;

  private Integer totalImported;

  private Integer totalProcessed;

  private String error;
}
