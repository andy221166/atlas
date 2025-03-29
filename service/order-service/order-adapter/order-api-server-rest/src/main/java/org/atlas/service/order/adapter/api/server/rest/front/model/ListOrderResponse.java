package org.atlas.service.order.adapter.api.server.rest.front.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.order.domain.shared.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOrderResponse {

  private PagingResult<Order> orderPage;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Order {

    private Integer id;
    private String code;
    private List<OrderItem> orderItems;
    private BigDecimal amount;
    private OrderStatus status;
    private String canceledReason;
    private Date createdAt;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItem {

    private Product product;
    private Integer quantity;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Product {

    private Integer id;
    private String name;
    private BigDecimal price;
  }
}
