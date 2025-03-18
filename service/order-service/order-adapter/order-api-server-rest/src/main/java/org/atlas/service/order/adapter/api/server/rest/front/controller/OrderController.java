package org.atlas.service.order.adapter.api.server.rest.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.adapter.api.server.rest.front.model.PlaceOrderRequest;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.port.inbound.usecase.front.PlaceOrderUseCase;
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

@RestController
@RequestMapping("api/front/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

  private PlaceOrderUseCase placeOrderUseCase;

  @GetMapping
  public Response<PageDto<OrderDto>> listOrder(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
  ) throws Exception {
    ListOrderQuery query = new ListOrderQuery();
    query.setPage(page - 1);
    query.setSize(size);
    return Response.success(queryGateway.send(query));
  }

  @GetMapping("/{id}")
  public Response<OrderEntity> getOrderStatus(@PathVariable("id") Integer id) throws Exception {
    GetOrderQuery query = new GetOrderQuery(id);
    return Response.success(queryGateway.send(query));
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
