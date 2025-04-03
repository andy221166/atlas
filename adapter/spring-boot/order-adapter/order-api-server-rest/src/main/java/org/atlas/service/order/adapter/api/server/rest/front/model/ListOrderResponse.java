package org.atlas.service.order.adapter.api.server.rest.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.domain.order.shared.OrderStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Response object containing the list of orders with pagination.")
public class ListOrderResponse extends PagingResult<org.atlas.service.order.adapter.api.server.rest.admin.model.ListOrderResponse.Order> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Represents an order in the order list.")
  public static class Order {

    @Schema(description = "Unique identifier of the order.", example = "1")
    private Integer id;

    @Schema(description = "Order code.", example = "ORD123456")
    private String code;

    @Schema(description = "List of items in the order.")
    private List<OrderItem> orderItems;

    @Schema(description = "Total amount of the order.", example = "99.99")
    private BigDecimal amount;

    @Schema(description = "Current status of the order.")
    private OrderStatus status;

    @Schema(description = "Reason for canceling the order, if applicable.")
    private String cancelReason;

    @Schema(description = "Date and time when the order was created.")
    private Date createdAt;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Represents an item in an order.")
  public static class OrderItem {

    @Schema(description = "Product associated with the order item.")
    private Product product;

    @Schema(description = "Quantity of the product in the order.", example = "2")
    private Integer quantity;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Represents a product in the order item.")
  public static class Product {

    @Schema(description = "Unique identifier of the product.", example = "1")
    private Integer id;

    @Schema(description = "Name of the product.", example = "Product Name")
    private String name;

    @Schema(description = "Price of the product.", example = "49.99")
    private BigDecimal price;
  }
}
