package org.atlas.service.aggregator.contract.query;

import lombok.Data;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.cqrs.model.Query;

@Data
public class ListOrderQuery implements Query<PageDto<AggOrder>> {

  private Integer page;
  private Integer size;
}
