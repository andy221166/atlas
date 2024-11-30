package org.atlas.service.report.contract.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ReportDto {

  private BigDecimal totalAmount;
  private List<OrderDto> topHighestAmountOrders;
  private List<ProductDto> topBestSoldProducts;
  private List<UserDto> topHighestSpentUsers;
}
