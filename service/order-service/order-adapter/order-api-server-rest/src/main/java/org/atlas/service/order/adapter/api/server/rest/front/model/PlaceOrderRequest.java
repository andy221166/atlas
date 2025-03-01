package org.atlas.service.order.adapter.api.server.rest.front.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class PlaceOrderRequest {

  @NotEmpty
  private List<@Valid OrderItem> orderItems;

  @Data
  public static class OrderItem {

    @NotNull
    private Integer productId;

    @NotNull
    @Min(1)
    private Integer quantity;
  }
}
