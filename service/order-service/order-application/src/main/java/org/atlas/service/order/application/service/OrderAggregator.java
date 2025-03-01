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
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderItemEntity;
import org.atlas.service.order.domain.entity.OrderStatus;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.client.user.UserApiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderAggregator {

  private final OrderRepository orderRepository;
  private final UserApiClient userApiClient;
  private final ProductApiClient productApiClient;

  public OrderDto aggregate(OrderEntity orderEntity, boolean hasUpdateProductPrice) {
    List<OrderDto> orderDtoList = aggregate(Collections.singletonList(orderEntity),
        hasUpdateProductPrice);
    return orderDtoList.get(0);
  }

  public List<OrderDto> aggregate(List<OrderEntity> orderEntities, boolean hasUpdateProductPrice) {
    Map<Integer, ProfileDto> userMap = fetchUsers(orderEntities);
    Map<Integer, ProductDto> productMap = fetchProducts(orderEntities);
    return orderEntities.stream()
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
  public OrderEntity findProcessingOrder(Integer id) {
    OrderEntity orderEntity = orderRepository.findById(id)
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
    if (!OrderStatus.PROCESSING.equals(orderEntity.getStatus())) {
      throw new BusinessException(AppError.ORDER_INVALID_STATUS);
    }
    return orderEntity;
  }

  private Map<Integer, ProfileDto> fetchUsers(List<OrderEntity> orderEntities) {
    List<Integer> userIds = orderEntities.stream()
        .map(OrderEntity::getUserId)
        .distinct()
        .toList();
    List<ProfileDto> users = userServiceClient.listUser(userIds);
    if (CollectionUtils.isEmpty(users)) {
      return Collections.emptyMap();
    }
    return users.stream()
        .collect(Collectors.toMap(ProfileDto::getId, Function.identity()));
  }

  private Map<Integer, ProductDto> fetchProducts(List<OrderEntity> orderEntities) {
    List<Integer> productIds = orderEntities.stream()
        .flatMap(order -> order.getOrderItemEntities()
            .stream()
            .map(OrderItemEntity::getProductId))
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

  public static OrderDto toOrderDto(OrderEntity orderEntity,
      Map<Integer, ProfileDto> userMap,
      Map<Integer, ProductDto> productMap,
      boolean hasUpdateProductPrice) {
    OrderDto orderDto = new OrderDto();
    orderDto.setId(orderEntity.getId());
    orderDto.setAmount(orderEntity.getAmount());
    orderDto.setStatus(orderEntity.getStatus());
    orderDto.setCanceledReason(orderEntity.getCanceledReason());
    orderDto.setCreatedAt(orderEntity.getCreatedAt());

    // User
    ProfileDto profileDto = userMap.get(orderEntity.getUserId());
    orderDto.setUser(profileDto);

    // Order items
    for (OrderItemEntity orderItemEntity : orderEntity.getOrderItemEntities()) {
      OrderItemDto orderItemDto = new OrderItemDto();
      ProductDto productDto = productMap.get(orderItemEntity.getProductId());
      orderItemDto.setProduct(productDto);
      orderItemDto.setQuantity(orderItemEntity.getQuantity());
      orderDto.addOrderItem(orderItemDto);

      if (hasUpdateProductPrice) {
        orderItemEntity.setProductPrice(productDto.getPrice());
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
