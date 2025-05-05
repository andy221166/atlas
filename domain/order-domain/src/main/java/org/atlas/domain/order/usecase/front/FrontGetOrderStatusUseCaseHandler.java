package org.atlas.domain.order.usecase.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.domain.order.usecase.front.FrontGetOrderStatusUseCaseHandler.GetOrderStatusInput;
import org.atlas.domain.order.usecase.front.FrontGetOrderStatusUseCaseHandler.GetOrderStatusOutput;
import org.atlas.framework.error.AppError;
import org.atlas.framework.domain.exception.DomainException;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontGetOrderStatusUseCaseHandler implements
    UseCaseHandler<GetOrderStatusInput, GetOrderStatusOutput> {

  private final OrderRepository orderRepository;

  @Override
  public GetOrderStatusOutput handle(GetOrderStatusInput input) throws Exception {
    OrderEntity orderEntity = orderRepository.findById(input.getOrderId())
        .orElseThrow(() -> new DomainException(AppError.ORDER_NOT_FOUND));
    return GetOrderStatusOutput.builder()
        .status(orderEntity.getStatus())
        .canceledReason(orderEntity.getCanceledReason())
        .build();
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetOrderStatusInput {

    private Integer orderId;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetOrderStatusOutput {

    private OrderStatus status;
    private String canceledReason;
  }
}
