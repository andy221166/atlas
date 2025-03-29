package org.atlas.service.order.port.inbound.admin;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.Pagination;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.atlas.service.order.port.inbound.admin.ListOrderUseCase.ListOrderInput;
import org.atlas.service.order.port.inbound.admin.ListOrderUseCase.ListOrderOutput;
import org.atlas.service.order.port.inbound.admin.ListOrderUseCase.ListOrderOutput.Order;

public interface ListOrderUseCase extends UseCase<ListOrderInput, ListOrderOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ListOrderInput {

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  class ListOrderOutput extends PagingResult<Order> {

    public ListOrderOutput() {
      this.results = Collections.emptyList();
      this.pagination = Pagination.empty();
    }

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
}
