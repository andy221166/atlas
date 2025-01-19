package org.atlas.service.user.contract.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.user.contract.model.ImportUserResultDto;

@Data
@AllArgsConstructor
public class GetImportUserResultQuery implements Query<ImportUserResultDto> {

  private Integer requestId;
}
