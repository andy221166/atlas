package org.atlas.service.order.application.usecase.front;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderItemEntity;
import org.atlas.service.order.port.inbound.usecase.front.ListOrderUseCase;
import org.atlas.service.order.port.inbound.usecase.front.ListOrderUseCase.Output.Order;
import org.atlas.service.order.port.inbound.usecase.front.ListOrderUseCase.Output.OrderItem;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase.Output.Product;
import org.springframework.stereotype.Component;

@Component("frontListOrderUseCaseHandler")
@RequiredArgsConstructor
public class ListOrderUseCaseHandler implements ListOrderUseCase {

  private final OrderRepositoryPort orderRepositoryPort;
  private final ProductApiClient productApiClient;

  @Override
  public Output handle(Input input) throws Exception {
    // Query order
    Integer userId = UserContext.getUserId();
    PagingResult<OrderEntity> orderEntityPage = orderRepositoryPort.findByUserId(userId,
        input.getPagingRequest());
    if (orderEntityPage.getTotalCount() == 0L) {
      return new Output(PagingResult.empty());
    }

    // Fetch products
    Map<Integer, Product> products = fetchProducts(orderEntityPage.getResults());

    PagingResult<Order> orderPage = orderEntityPage.map(orderEntity -> {
          Order order = ObjectMapperUtil.getInstance().map(orderEntity, Order.class);

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
        .collect(Collectors.toMap(ListProductUseCase.Output.Product::getId, Function.identity()));
  }
}
