package org.atlas.service.order.application.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.api.client.contract.product.IProductServiceClient;
import org.atlas.platform.api.client.contract.user.IUserServiceClient;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.model.OrderItemDto;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.OrderItem;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.atlas.service.product.contract.model.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final IUserServiceClient userServiceClient;
  private final IProductServiceClient productServiceClient;

  public OrderDto aggregate(Order order, boolean hasUpdateProductPrice) {
    List<OrderDto> orderDtoList = aggregate(Collections.singletonList(order),
        hasUpdateProductPrice);
    return orderDtoList.get(0);
  }

  public List<OrderDto> aggregate(List<Order> orders, boolean hasUpdateProductPrice) {
    Map<Integer, ProfileDto> userMap = fetchUsers(orders);
    Map<Integer, ProductDto> productMap = fetchProducts(orders);
    return orders.stream()
        .map(order -> {
          OrderDto orderDto = toOrderDto(order, userMap, productMap, hasUpdateProductPrice);

          // Calculate order amount
          BigDecimal amount = calculateAmount(orderDto);
          order.setAmount(amount);
          orderDto.setAmount(amount);

          return orderDto;
        })
        .toList();
  }

  @Transactional(readOnly = true)
  public Order findProcessingOrder(Integer id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
    if (!OrderStatus.PROCESSING.equals(order.getStatus())) {
      throw new BusinessException(AppError.ORDER_INVALID_STATUS);
    }
    return order;
  }

  private Map<Integer, ProfileDto> fetchUsers(List<Order> orders) {
    List<Integer> userIds = orders.stream()
        .map(Order::getUserId)
        .distinct()
        .toList();
    List<ProfileDto> users = userServiceClient.listUser(userIds);
    if (CollectionUtils.isEmpty(users)) {
      return Collections.emptyMap();
    }
    return users.stream()
        .collect(Collectors.toMap(ProfileDto::getId, Function.identity()));
  }

  private Map<Integer, ProductDto> fetchProducts(List<Order> orders) {
    List<Integer> productIds = orders.stream()
        .flatMap(order -> order.getOrderItems()
            .stream()
            .map(OrderItem::getProductId))
        .distinct()
        .toList();
    List<ProductDto> products = productServiceClient.listProduct(productIds);

    Map<Integer, ProductDto> productDtoMap = products.stream()
        .collect(Collectors.toMap(ProductDto::getId, Function.identity()));

    // Verify if all product IDs exist
    Set<Integer> productIdsSet = new HashSet<>(productIds);
    Set<Integer> productDtoIdsSet = productDtoMap.keySet();
    if (!productIdsSet.equals(productDtoIdsSet)) {
      throw new BusinessException(AppError.PRODUCT_NOT_FOUND);
    }

    return productDtoMap;
  }

  public static OrderDto toOrderDto(Order order,
      Map<Integer, ProfileDto> userMap,
      Map<Integer, ProductDto> productMap,
      boolean hasUpdateProductPrice) {
    OrderDto orderDto = new OrderDto();
    orderDto.setId(order.getId());
    orderDto.setAmount(order.getAmount());
    orderDto.setStatus(order.getStatus());
    orderDto.setCanceledReason(order.getCanceledReason());
    orderDto.setCreatedAt(order.getCreatedAt());

    // User
    ProfileDto profileDto = userMap.get(order.getUserId());
    orderDto.setUser(profileDto);

    // Order items
    for (OrderItem orderItem : order.getOrderItems()) {
      OrderItemDto orderItemDto = new OrderItemDto();
      ProductDto productDto = productMap.get(orderItem.getProductId());
      orderItemDto.setProduct(productDto);
      orderItemDto.setQuantity(orderItem.getQuantity());
      orderDto.addOrderItem(orderItemDto);

      if (hasUpdateProductPrice) {
        orderItem.setProductPrice(productDto.getPrice());
      }
    }

    return orderDto;
  }

  private BigDecimal calculateAmount(OrderDto orderDto) {
    BigDecimal amount = BigDecimal.ZERO;
    for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
      BigDecimal subAmount = orderItemDto.getProduct()
          .getPrice()
          .multiply(BigDecimal.valueOf(orderItemDto.getQuantity()));
      amount = amount.add(subAmount);
    }
    return amount;
  }
}
