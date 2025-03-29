package org.atlas.service.order.adapter.api.server.rest.admin.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.order.adapter.api.server.rest.admin.model.ListOrderResponse.Order;
import org.atlas.service.order.domain.shared.OrderStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListOrderResponse extends PagingResult<Order> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Order {

    private Integer id;
    private String code;
    private User user;
    private List<OrderItem> orderItems;
    private BigDecimal amount;
    private OrderStatus status;
    private String cancelReason;
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
  public static class User {

    private Integer id;
    private String firstName;
    private String lastName;
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
