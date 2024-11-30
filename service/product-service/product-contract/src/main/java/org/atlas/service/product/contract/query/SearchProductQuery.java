package org.atlas.service.product.contract.query;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.product.contract.model.ProductDto;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchProductQuery extends PagingRequest implements Query<PageDto<ProductDto>> {

  private String keyword;
  private Integer categoryId;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
}
