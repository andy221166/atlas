package org.atlas.infrastructure.api.server.rest.adapter.order.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.usecase.front.FrontGetOrderStatusUseCaseHandler;
import org.atlas.domain.order.usecase.front.FrontGetOrderStatusUseCaseHandler.GetOrderStatusInput;
import org.atlas.domain.order.usecase.front.FrontGetOrderStatusUseCaseHandler.GetOrderStatusOutput;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.ListOrderInput;
import org.atlas.domain.order.usecase.front.FrontListOrderUseCaseHandler.ListOrderOutput;
import org.atlas.domain.order.usecase.front.FrontPlaceOrderUseCaseHandler;
import org.atlas.domain.order.usecase.front.FrontPlaceOrderUseCaseHandler.PlaceOrderInput;
import org.atlas.domain.order.usecase.front.FrontPlaceOrderUseCaseHandler.PlaceOrderOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.infrastructure.api.server.rest.adapter.order.front.model.GetOrderStatusResponse;
import org.atlas.infrastructure.api.server.rest.adapter.order.front.model.ListOrderResponse;
import org.atlas.infrastructure.api.server.rest.adapter.order.front.model.PlaceOrderRequest;
import org.atlas.infrastructure.api.server.rest.adapter.order.front.model.PlaceOrderResponse;
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

@RestController
@RequestMapping("api/front/orders")
@Validated
@RequiredArgsConstructor
public class FrontOrderController {

  private final FrontListOrderUseCaseHandler frontListOrderUseCaseHandler;
  private final FrontGetOrderStatusUseCaseHandler frontGetOrderStatusUseCaseHandler;
  private final FrontPlaceOrderUseCaseHandler frontPlaceOrderUseCaseHandler;

  @Operation(summary = "List Orders", description = "Retrieves a paginated list of orders for the front-end.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<ListOrderResponse> listOrder(
      @Parameter(name = "page", description = "The page number to retrieve (default is 1).", example = "1")
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(name = "size", description = "The number of orders per page (default is defined by the constant).", example = "10")
      @RequestParam(name = "size", required = false, defaultValue = CommonConstant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListOrderInput input = ListOrderInput.builder()
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    ListOrderOutput output = frontListOrderUseCaseHandler.handle(input);
    ListOrderResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListOrderResponse.class);
    return ApiResponseWrapper.success(response);
  }

  @Operation(summary = "Get Order Status", description = "Retrieves the status of a specific order by its ID.")
  @GetMapping(value = "/{orderId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<GetOrderStatusResponse> getOrderStatus(
      @Parameter(name = "orderId", description = "ID of the order to retrieve the status for.", example = "123")
      @PathVariable("orderId") Integer orderId) throws Exception {
    GetOrderStatusInput input = GetOrderStatusInput.builder()
        .orderId(orderId)
        .build();
    GetOrderStatusOutput output = frontGetOrderStatusUseCaseHandler.handle(input);
    GetOrderStatusResponse response = ObjectMapperUtil.getInstance().map(output, GetOrderStatusResponse.class);
    return ApiResponseWrapper.success(response);
  }

  @Operation(summary = "Place Order", description = "Places a new order based on the provided order details.")
  @PostMapping(value = "/place", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponseWrapper<PlaceOrderResponse> placeOrder(
      @Parameter(description = "Order details to create a new order.", required = true)
      @Valid @RequestBody PlaceOrderRequest request) throws Exception {
    PlaceOrderInput input = ObjectMapperUtil.getInstance().map(request, PlaceOrderInput.class);
    PlaceOrderOutput output = frontPlaceOrderUseCaseHandler.handle(input);
    PlaceOrderResponse response = ObjectMapperUtil.getInstance().map(output, PlaceOrderResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
