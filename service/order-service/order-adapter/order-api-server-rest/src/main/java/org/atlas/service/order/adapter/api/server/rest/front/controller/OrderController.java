package org.atlas.service.order.adapter.api.server.rest.front.controller;

import jakarta.validation.Valid;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.adapter.api.server.rest.front.model.GetOrderStatusResponse;
import org.atlas.service.order.adapter.api.server.rest.front.model.ListOrderResponse;
import org.atlas.service.order.adapter.api.server.rest.front.model.PlaceOrderRequest;
import org.atlas.service.order.adapter.api.server.rest.front.model.PlaceOrderResponse;
import org.atlas.service.order.port.inbound.front.GetOrderStatusUseCase;
import org.atlas.service.order.port.inbound.front.GetOrderStatusUseCase.GetOrderStatusInput;
import org.atlas.service.order.port.inbound.front.GetOrderStatusUseCase.GetOrderStatusOutput;
import org.atlas.service.order.port.inbound.front.ListOrderUseCase;
import org.atlas.service.order.port.inbound.front.ListOrderUseCase.ListOrderInput;
import org.atlas.service.order.port.inbound.front.ListOrderUseCase.ListOrderOutput;
import org.atlas.service.order.port.inbound.front.PlaceOrderUseCase;
import org.atlas.service.order.port.inbound.front.PlaceOrderUseCase.PlaceOrderInput;
import org.atlas.service.order.port.inbound.front.PlaceOrderUseCase.PlaceOrderOutput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListOrderResponse> listOrder(
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer size
  ) throws Exception {
    ListOrderInput input = ListOrderInput.builder()
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    ListOrderOutput output = listOrderUseCase.handle(input);
    ListOrderResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListOrderResponse.class);
    return Response.success(response);
  }

  @GetMapping(value = "/{orderId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<GetOrderStatusResponse> getOrderStatus(
      @PathVariable("orderId") Integer orderId) throws Exception {
    GetOrderStatusInput input = GetOrderStatusInput.builder()
        .orderId(orderId)
        .build();
    GetOrderStatusOutput output = getOrderStatusUseCase.handle(input);
    GetOrderStatusResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetOrderStatusResponse.class);
    return Response.success(response);
  }

  @PostMapping(value = "/place", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Response<PlaceOrderResponse> placeOrder(
      @Valid @RequestBody PlaceOrderRequest request)
      throws Exception {
    PlaceOrderInput input = ObjectMapperUtil.getInstance()
        .map(request, PlaceOrderInput.class);
    PlaceOrderOutput output = placeOrderUseCase.handle(input);
    PlaceOrderResponse response = ObjectMapperUtil.getInstance()
        .map(output, PlaceOrderResponse.class);
    return Response.success(response);
  }
}
