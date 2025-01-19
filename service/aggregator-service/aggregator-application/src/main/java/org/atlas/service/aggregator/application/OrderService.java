package org.atlas.service.aggregator.application;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.platform.api.client.contract.product.IProductServiceClient;
import org.atlas.platform.api.client.contract.order.IOrderServiceClient;
import org.atlas.platform.event.contract.order.payload.OrderItemPayload;
import org.atlas.platform.event.contract.order.payload.OrderPayload;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.user.contract.model.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final IOrderServiceClient userServiceClient;
  private final IProductServiceClient productServiceClient;

  public OrderPayload aggregate(org.atlas.service.order.domain.Order order, boolean hasUpdateProductPrice) {
    List<OrderPayload> orderPayloadList = aggregate(Collections.singletonList(order),
        hasUpdateProductPrice);
    return orderPayloadList.get(0);
  }

  public List<OrderPayload> aggregate(List<org.atlas.service.order.domain.Order> orders, boolean hasUpdateProductPrice) {
    Map<Integer, UserDto> userMap = fetchUsers(orders);
    Map<Integer, ProductDto> productMap = fetchProducts(orders);
    return orders.stream()
        .map(order -> {
          OrderPayload orderPayload = toOrderDto(order, userMap, productMap, hasUpdateProductPrice);

          // Calculate order amount
          BigDecimal amount = calculateAmount(orderPayload);
          order.setAmount(amount);
          orderPayload.setAmount(amount);

          return orderPayload;
        })
        .toList();
  }

  private BigDecimal calculateAmount(OrderPayload orderPayload) {
    BigDecimal amount = BigDecimal.ZERO;
    for (OrderItemPayload orderItemPayloadDto : orderPayload.getOrderItemPayloads()) {
      BigDecimal subAmount = orderItemPayloadDto.getProductPayload()
          .getPrice()
          .multiply(BigDecimal.valueOf(orderItemPayloadDto.getQuantity()));
      amount = amount.add(subAmount);
    }
    return amount;
  }
}
