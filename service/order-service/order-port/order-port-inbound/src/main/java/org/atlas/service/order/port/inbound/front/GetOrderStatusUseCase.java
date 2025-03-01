package org.atlas.service.order.port.inbound.front;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.order.domain.entity.OrderStatus;

public interface GetOrderStatusUseCase
    extends UseCase<GetOrderStatusUseCase.Input, GetOrderStatusUseCase.Output> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private Integer orderId;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private OrderStatus status;
  }
}
