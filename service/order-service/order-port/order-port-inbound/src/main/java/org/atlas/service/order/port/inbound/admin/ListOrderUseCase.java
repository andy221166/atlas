package org.atlas.service.order.port.inbound.admin;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.order.domain.entity.OrderStatus;

public interface ListOrderUseCase
    extends UseCase<ListOrderUseCase.Input, ListOrderUseCase.Output> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private OrderStatus status;
    private Date startDate;
    private Date endDate;
    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private PagingResult<Order> orders;

    @Data
    public static class Order {

      private Integer id;
      private String code;
      private User user;
      private List<OrderItem> orderItems;
      private BigDecimal amount;
      private OrderStatus status;
      private Date createdDate;
    }

    @Data
    public static class OrderItem {

      private Product product;
      private Integer quantity;
    }

    @Data
    public static class User {

      private Integer id;
      private String firstName;
      private String lastName;
    }

    @Data
    public static class Product {

      private Integer id;
      private String name;
      private BigDecimal price;
    }
  }
}
