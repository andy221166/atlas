package org.atlas.domain.order.usecase.front;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.entity.OrderItemEntity;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.ListOrderInput;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.ListOrderOutput;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.ListOrderOutput.Order;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.ListOrderOutput.OrderItem;
import org.atlas.domain.product.shared.internal.ListProductInput;
import org.atlas.domain.product.shared.internal.ListProductOutput;
import org.atlas.domain.product.shared.internal.ListProductOutput.Product;
import org.atlas.framework.security.UserContext;
import org.atlas.framework.internalapi.ProductApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontListOrderUseCaseHandler implements UseCaseHandler<ListOrderInput, ListOrderOutput> {

  private final OrderRepository orderRepository;
  private final ProductApiPort productApiPort;

  @Override
  public ListOrderOutput handle(ListOrderInput input) throws Exception {
    // Query order
    Integer userId = UserContext.getUserId();
    PagingResult<OrderEntity> orderEntityPage = orderRepository.findByUserId(userId,
        input.getPagingRequest());
    if (orderEntityPage.checkEmpty()) {
      return new ListOrderOutput();
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

  private Map<Integer, Product> fetchProducts(List<OrderEntity> orderEntities) {
    List<Integer> productIds = orderEntities.stream()
        .flatMap(orderEntity -> orderEntity.getOrderItems()
            .stream()
            .map(OrderItemEntity::getProductId))
        .distinct()
        .toList();
    ListProductInput input = new ListProductInput(productIds);
    ListProductOutput output = productApiPort.call(input);
    return output.getProducts()
        .stream()
        .collect(Collectors.toMap(Product::getId, Function.identity()));
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListOrderInput {

    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ListOrderOutput extends PagingResult<ListOrderOutput.Order> {

    public ListOrderOutput() {
      this.results = Collections.emptyList();
      this.pagination = Pagination.empty();
    }

    public ListOrderOutput(List<Order> results, Pagination pagination) {
      super(results, pagination);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {

      private Integer id;
      private String code;
      private List<OrderItem> orderItems;
      private BigDecimal amount;
      private OrderStatus status;
      private String canceledReason;
      private Date createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

      private Product product;
      private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

      private Integer id;
      private String name;
      private BigDecimal price;
    }
  }
}
