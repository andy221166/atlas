package org.atlas.service.order.port.inbound.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.atlas.service.order.port.inbound.front.GetOrderStatusUseCase.GetOrderStatusInput;
import org.atlas.service.order.port.inbound.front.GetOrderStatusUseCase.GetOrderStatusOutput;

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
