package org.atlas.domain.order.usecase.front;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.entity.OrderItemEntity;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.ListOrderInput;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.OrderOutput;
import org.atlas.domain.product.shared.internal.ListProductInput;
import org.atlas.domain.product.shared.internal.ListProductOutput;
import org.atlas.domain.product.shared.internal.ListProductOutput.Product;
import org.atlas.framework.context.Contexts;
import org.atlas.framework.internalapi.ProductApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingRequest.SortOrder;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontListOrderUseCaseHandler implements
    UseCaseHandler<ListOrderInput, PagingResult<OrderOutput>> {

  private final OrderRepository orderRepository;
  private final ProductApiPort productApiPort;

  @Override
  public PagingResult<OrderOutput> handle(ListOrderInput input) throws Exception {
    // Query order
    Integer userId = Contexts.getUserId();
    input.getPagingRequest().setSortBy("createdAt");
    input.getPagingRequest().setSortOrder(SortOrder.DESC);
    PagingResult<OrderEntity> orderEntityPage = orderRepository.findByUserId(userId,
        input.getPagingRequest());
    if (orderEntityPage.checkEmpty()) {
      return PagingResult.empty();
    }

    // Fetch products
    Map<Integer, Product> products = fetchProducts(orderEntityPage.getData());

    return orderEntityPage.map(orderEntity -> {
      OrderOutput orderOutput = ObjectMapperUtil.getInstance().map(orderEntity, OrderOutput.class);

      List<OrderItemOutput> orderItemOutputs = orderEntity.getOrderItems()
          .stream()
          .map(orderItemEntity -> {
            OrderItemOutput orderItemOutput = ObjectMapperUtil.getInstance()
                .map(orderItemEntity, OrderItemOutput.class);

            Product product = products.get(orderItemEntity.getProductId());
            if (product != null) {
              orderItemOutput.setProduct(
                  ObjectMapperUtil.getInstance().map(product, ProductOutput.class));
            }

            return orderItemOutput;
          })
          .toList();
      orderOutput.setOrderItems(orderItemOutputs);

      return orderOutput;
    });
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
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderOutput {

    private Integer id;
    private String code;
    private List<OrderItemOutput> orderItems;
    private BigDecimal amount;
    private OrderStatus status;
    private String canceledReason;
    private Date createdAt;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItemOutput {

    private ProductOutput product;
    private Integer quantity;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductOutput {

    private Integer id;
    private String name;
    private BigDecimal price;
  }
}
