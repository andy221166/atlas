package org.atlas.service.order.port.inbound.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;

public interface GetOrderStatusUseCase
    extends UseCase<GetOrderStatusUseCase.Input, GetOrderStatusUseCase.Output> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private Integer orderId;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private OrderStatus status;
  }
}
