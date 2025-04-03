package org.atlas.port.inbound.order.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.port.inbound.order.front.GetOrderStatusUseCase.GetOrderStatusInput;
import org.atlas.port.inbound.order.front.GetOrderStatusUseCase.GetOrderStatusOutput;

public interface GetOrderStatusUseCase extends UseCase<GetOrderStatusInput, GetOrderStatusOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class GetOrderStatusInput {

    private Integer orderId;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class GetOrderStatusOutput {

    private OrderStatus status;
  }
}
