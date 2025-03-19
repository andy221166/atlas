package org.atlas.service.order.application.service;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepositoryPort orderRepositoryPort;

  @Transactional(readOnly = true)
  public OrderEntity findProcessingOrder(Integer orderId) {
    OrderEntity orderEntity = orderRepositoryPort.findById(orderId)
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
    if (orderEntity.getStatus() != OrderStatus.PROCESSING) {
      throw new BusinessException(AppError.ORDER_INVALID_STATUS);
    }
    return orderEntity;
  }
}
