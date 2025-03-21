package org.atlas.service.order.adapter.api.server.rest.front.controller;

import jakarta.validation.Valid;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.adapter.api.server.rest.front.model.PlaceOrderRequest;
import org.atlas.service.order.port.inbound.usecase.front.GetOrderStatusUseCase;
import org.atlas.service.order.port.inbound.usecase.front.ListOrderUseCase;
import org.atlas.service.order.port.inbound.usecase.front.PlaceOrderUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontOrderController")
@RequestMapping("api/front/orders")
@Validated
public class OrderController {

  private final ListOrderUseCase listOrderUseCase;
  private final GetOrderStatusUseCase getOrderStatusUseCase;
  private final PlaceOrderUseCase placeOrderUseCase;

  public OrderController(
      @Qualifier("frontListOrderUseCaseHandler") ListOrderUseCase listOrderUseCase,
      GetOrderStatusUseCase getOrderStatusUseCase,
      PlaceOrderUseCase placeOrderUseCase) {
    this.listOrderUseCase = listOrderUseCase;
    this.getOrderStatusUseCase = getOrderStatusUseCase;
    this.placeOrderUseCase = placeOrderUseCase;
  }

  @GetMapping
  public Response<ListOrderUseCase.Output> listOrder(
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer size
  ) throws Exception {
    ListOrderUseCase.Input input = ListOrderUseCase.Input.builder()
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    ListOrderUseCase.Output output = listOrderUseCase.handle(input);
    return Response.success(output);
  }

  @GetMapping("/{orderId}")
  public Response<GetOrderStatusUseCase.Output> getOrderStatus(
      @PathVariable("orderId") Integer orderId) throws Exception {
    GetOrderStatusUseCase.Input input = GetOrderStatusUseCase.Input.builder()
        .orderId(orderId)
        .build();
    GetOrderStatusUseCase.Output output = getOrderStatusUseCase.handle(input);
    return Response.success(output);
  }

  @PostMapping("/place")
  @ResponseStatus(HttpStatus.CREATED)
  public Response<Integer> placeOrder(@Valid @RequestBody PlaceOrderRequest request)
      throws Exception {
    PlaceOrderUseCase.Input input = ObjectMapperUtil.getInstance()
        .map(request, PlaceOrderUseCase.Input.class);
    request.getOrderItems()
        .forEach(orderItem -> input.addOrderItem(ObjectMapperUtil.getInstance()
            .map(orderItem, PlaceOrderUseCase.Input.OrderItem.class)));
    PlaceOrderUseCase.Output output = placeOrderUseCase.handle(input);
    return Response.success(output.getOrderId());
  }
}
