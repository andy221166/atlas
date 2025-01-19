package org.atlas.service.user.contract.model;

import lombok.Data;
import org.atlas.service.user.domain.ImportUserRequestStatus;

@Data
public class ImportUserResultDto {

  private ImportUserRequestStatus status;
  private Integer totalImported;
  private Integer totalProcessed;
  private String error;
}
