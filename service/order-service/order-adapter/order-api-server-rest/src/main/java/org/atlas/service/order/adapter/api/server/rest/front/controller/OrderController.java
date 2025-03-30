package org.atlas.service.order.adapter.api.server.rest.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OrderController {

  private final ListOrderUseCase listOrderUseCase;
  private final GetOrderStatusUseCase getOrderStatusUseCase;
  private final PlaceOrderUseCase placeOrderUseCase;

  @Operation(summary = "List Orders", description = "Retrieves a paginated list of orders for the front-end.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListOrderResponse> listOrder(
      @Parameter(name = "page", description = "The page number to retrieve (default is 1).", example = "1")
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(name = "size", description = "The number of orders per page (default is defined by the constant).", example = "10")
      @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListOrderInput input = ListOrderInput.builder()
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    ListOrderOutput output = listOrderUseCase.handle(input);
    ListOrderResponse response = ObjectMapperUtil.getInstance().map(output, ListOrderResponse.class);
    return Response.success(response);
  }

  @Operation(summary = "Get Order Status", description = "Retrieves the status of a specific order by its ID.")
  @GetMapping(value = "/{orderId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<GetOrderStatusResponse> getOrderStatus(
      @Parameter(name = "orderId", description = "ID of the order to retrieve the status for.", example = "123")
      @PathVariable("orderId") Integer orderId) throws Exception {
    GetOrderStatusInput input = GetOrderStatusInput.builder()
        .orderId(orderId)
        .build();
    GetOrderStatusOutput output = getOrderStatusUseCase.handle(input);
    GetOrderStatusResponse response = ObjectMapperUtil.getInstance().map(output, GetOrderStatusResponse.class);
    return Response.success(response);
  }

  @Operation(summary = "Place Order", description = "Places a new order based on the provided order details.")
  @PostMapping(value = "/place", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Response<PlaceOrderResponse> placeOrder(
      @Parameter(description = "Order details to create a new order.", required = true)
      @Valid @RequestBody PlaceOrderRequest request) throws Exception {
    PlaceOrderInput input = ObjectMapperUtil.getInstance().map(request, PlaceOrderInput.class);
    PlaceOrderOutput output = placeOrderUseCase.handle(input);
    PlaceOrderResponse response = ObjectMapperUtil.getInstance().map(output, PlaceOrderResponse.class);
    return Response.success(response);
  }
}
