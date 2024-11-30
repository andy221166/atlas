package org.atlas.service.report.contract.query;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.report.contract.model.ReportDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportQuery implements Query<ReportDto> {

  private Date startDate;
  private Date endDate;
}
