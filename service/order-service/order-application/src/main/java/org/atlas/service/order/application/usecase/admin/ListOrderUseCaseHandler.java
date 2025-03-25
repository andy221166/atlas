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
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderItemEntity;
import org.atlas.service.order.port.inbound.usecase.admin.ListOrderUseCase;
import org.atlas.service.order.port.inbound.usecase.admin.ListOrderUseCase.Output.Order;
import org.atlas.service.order.port.inbound.usecase.admin.ListOrderUseCase.Output.OrderItem;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase.Output.Product;
import org.atlas.service.user.port.inbound.usecase.internal.ListUserUseCase;
import org.atlas.service.user.port.inbound.usecase.internal.ListUserUseCase.Output.User;
import org.springframework.stereotype.Component;

@Component("adminListOrderUseCaseHandler")
@RequiredArgsConstructor
public class ListOrderUseCaseHandler implements ListOrderUseCase {

  private final OrderRepositoryPort orderRepositoryPort;
  private final UserApiClient userApiClient;
  private final ProductApiClient productApiClient;

  @Override
  public Output handle(Input input) throws Exception {
    // Query order
    PagingResult<OrderEntity> orderEntityPage = orderRepositoryPort.findAll(input.getPagingRequest());
    if (orderEntityPage.isEmpty()) {
      return new Output(PagingResult.empty());
    }

    // Fetch products
    Map<Integer, User> users = fetchUsers(orderEntityPage.getData());
    Map<Integer, Product> products = fetchProducts(orderEntityPage.getData());

    PagingResult<Order> orderPage = orderEntityPage.map(orderEntity -> {
      Order order = ObjectMapperUtil.getInstance().map(orderEntity, Order.class);

      User user = users.get(orderEntity.getUserId());
      if (user != null) {
        order.setUser(
            ObjectMapperUtil.getInstance().map(user, Output.User.class));
      }

      List<OrderItem> orderItems = orderEntity.getOrderItems()
          .stream()
          .map(orderItemEntity -> {
            OrderItem orderItem = ObjectMapperUtil.getInstance()
                .map(orderItemEntity, OrderItem.class);

            Product product = products.get(orderItemEntity.getProductId());
            if (product != null) {
              orderItem.setProduct(
                  ObjectMapperUtil.getInstance().map(product, Output.Product.class));
            }

            return orderItem;
          })
          .toList();
      order.setOrderItems(orderItems);

      return order;
    });

    return new Output(orderPage);
  }

  private Map<Integer, User> fetchUsers(List<OrderEntity> orderEntities) {
    List<Integer> userIds = orderEntities.stream()
        .map(OrderEntity::getUserId)
        .distinct()
        .toList();
    ListUserUseCase.Input input = new ListUserUseCase.Input(userIds);
    ListUserUseCase.Output output = userApiClient.call(input);
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
    ListProductUseCase.Input input = new ListProductUseCase.Input(productIds);
    ListProductUseCase.Output output = productApiClient.call(input);
    return output.getProducts()
        .stream()
        .collect(Collectors.toMap(Product::getId, Function.identity()));
  }
}
