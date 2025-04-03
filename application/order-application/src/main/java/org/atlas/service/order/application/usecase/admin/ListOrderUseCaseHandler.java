package org.atlas.service.order.application.usecase.admin;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.entity.OrderItemEntity;
import org.atlas.port.inbound.order.admin.ListOrderUseCase;
import org.atlas.port.inbound.order.admin.ListOrderUseCase.ListOrderOutput.Order;
import org.atlas.port.inbound.order.admin.ListOrderUseCase.ListOrderOutput.OrderItem;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductInput;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductOutput;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductOutput.Product;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserInput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserOutput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserOutput.User;
import org.springframework.stereotype.Component;

@Component("adminListOrderUseCaseHandler")
@RequiredArgsConstructor
public class ListOrderUseCaseHandler implements ListOrderUseCase {

  private final OrderRepositoryPort orderRepositoryPort;
  private final UserApiClient userApiClient;
  private final ProductApiClient productApiClient;

  @Override
  public ListOrderOutput handle(ListOrderInput input) throws Exception {
    // Query order
    PagingResult<OrderEntity> orderEntityPage = orderRepositoryPort.findAll(input.getPagingRequest());
    if (orderEntityPage.checkEmpty()) {
      return new ListOrderOutput();
    }

    // Fetch products
    Map<Integer, User> users = fetchUsers(orderEntityPage.getResults());
    Map<Integer, Product> products = fetchProducts(orderEntityPage.getResults());

    PagingResult<Order> orderPage = orderEntityPage.map(orderEntity -> {
      Order order = ObjectMapperUtil.getInstance().map(orderEntity, Order.class);

      User user = users.get(orderEntity.getUserId());
      if (user != null) {
        order.setUser(
            ObjectMapperUtil.getInstance().map(user, ListOrderOutput.User.class));
      }

      List<OrderItem> orderItems = orderEntity.getOrderItems()
          .stream()
          .map(orderItemEntity -> {
            OrderItem orderItem = ObjectMapperUtil.getInstance()
                .map(orderItemEntity, OrderItem.class);

            Product product = products.get(orderItemEntity.getProductId());
            if (product != null) {
              orderItem.setProduct(
                  ObjectMapperUtil.getInstance().map(product, ListOrderOutput.Product.class));
            }

            return orderItem;
          })
          .toList();
      order.setOrderItems(orderItems);

      return order;
    });

    return new ListOrderOutput(orderPage.getResults(), orderPage.getPagination());
  }

  private Map<Integer, User> fetchUsers(List<OrderEntity> orderEntities) {
    List<Integer> userIds = orderEntities.stream()
        .map(OrderEntity::getUserId)
        .distinct()
        .toList();
    ListUserInput input = new ListUserInput(userIds);
    ListUserOutput output = userApiClient.call(input);
    return output.getUsers()
        .stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));
  }

  private Map<Integer, Product> fetchProducts(List<OrderEntity> orderEntities) {
    List<Integer> productIds = orderEntities.stream()
        .flatMap(orderEntity -> orderEntity.getOrderItems()
            .stream()
            .map(OrderItemEntity::getProductId))
        .distinct()
        .toList();
    ListProductInput input = new ListProductInput(productIds);
    ListProductOutput output = productApiClient.call(input);
    return output.getProducts()
        .stream()
        .collect(Collectors.toMap(Product::getId, Function.identity()));
  }
}
