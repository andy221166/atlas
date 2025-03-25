package org.atlas.service.order.port.inbound.usecase.front;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.order.domain.shared.OrderStatus;

public interface ListOrderUseCase
    extends UseCase<ListOrderUseCase.Input, ListOrderUseCase.Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

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
      private Date createdDate;
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
}
