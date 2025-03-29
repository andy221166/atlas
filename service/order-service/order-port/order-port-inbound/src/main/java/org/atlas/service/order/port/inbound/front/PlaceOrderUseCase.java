package org.atlas.service.order.port.inbound.front;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.order.port.inbound.front.PlaceOrderUseCase.PlaceOrderInput;
import org.atlas.service.order.port.inbound.front.PlaceOrderUseCase.PlaceOrderOutput;

public interface PlaceOrderUseCase extends UseCase<PlaceOrderInput, PlaceOrderOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class PlaceOrderInput {

    @NotEmpty
    private List<@Valid OrderItem> orderItems;

    public void addOrderItem(OrderItem orderItem) {
      if (orderItems == null) {
        orderItems = new ArrayList<>();
      }
      orderItems.add(orderItem);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

      @NotNull
      private Integer productId;

      @NotNull
      @Min(1)
      private Integer quantity;
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class PlaceOrderOutput {

    private Integer orderId;
  }
}
