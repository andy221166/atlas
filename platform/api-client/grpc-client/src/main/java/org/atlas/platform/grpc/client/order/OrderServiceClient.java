package org.atlas.platform.grpc.client.order;

import java.math.BigDecimal;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.commons.constant.Constant;
import org.atlas.commons.model.PageDto;
import org.atlas.commons.util.DateUtil;
import org.atlas.platform.api.client.contract.order.IOrderServiceClient;
import org.atlas.platform.grpc.protobuf.order.ListOrderRequestProto;
import org.atlas.platform.grpc.protobuf.order.ListOrderResponseProto;
import org.atlas.platform.grpc.protobuf.order.OrderItemProto;
import org.atlas.platform.grpc.protobuf.order.OrderProto;
import org.atlas.platform.grpc.protobuf.order.OrderServiceGrpc;
import org.atlas.service.order.contract.dto.OrderDto;
import org.atlas.service.order.contract.dto.OrderItemDto;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceClient implements IOrderServiceClient {

  @GrpcClient("order-service")
  private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

  @Override
  public PageDto<OrderDto> listOrder(Integer page, Integer size) {
    ListOrderRequestProto requestProto = map(page, size);
    ListOrderResponseProto responseProto = orderServiceBlockingStub.listOrder(requestProto);
    return map(responseProto);
  }

  private ListOrderRequestProto map(Integer page, Integer size) {
    return ListOrderRequestProto.newBuilder()
        .setPage(page)
        .setSize(size)
        .build();
  }

  private PageDto<OrderDto> map(ListOrderResponseProto responseProto) {
    List<OrderDto> orderDtoList = responseProto.getRecordsList()
        .stream().map(this::map)
        .toList();
    return PageDto.of(orderDtoList, responseProto.getTotalCount());
  }

  private OrderDto map(OrderProto orderProto) {
    List<OrderItemDto> orderItemDtoList = orderProto.getOrderItemList()
        .stream()
        .map(this::map)
        .toList();
    OrderDto orderDto = new OrderDto();
    orderDto.setId(orderProto.getId());
    orderDto.setUserId(orderProto.getUserId());
    orderDto.setOrderItems(orderItemDtoList);
    orderDto.setAmount(BigDecimal.valueOf(orderProto.getAmount()));
    orderDto.setStatus(OrderStatus.valueOf(orderProto.getStatus()));
    orderDto.setCreatedAt(DateUtil.parse(orderProto.getCreatedAt(), Constant.DATE_TIME_FORMAT));
    return orderDto;
  }

  private OrderItemDto map(OrderItemProto orderItemProto) {
    OrderItemDto orderItemDto = new OrderItemDto();
    orderItemDto.setProductId(orderItemProto.getProductId());
    orderItemDto.setProductPrice(BigDecimal.valueOf(orderItemProto.getProductPrice()));
    orderItemDto.setQuantity(orderItemProto.getQuantity());
    return orderItemDto;
  }
}
