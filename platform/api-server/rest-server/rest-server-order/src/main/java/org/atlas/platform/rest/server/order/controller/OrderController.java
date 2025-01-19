package org.atlas.platform.rest.server.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.commons.enums.Role;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.cqrs.gateway.CommandGateway;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.rest.server.core.response.Response;
import org.atlas.service.order.contract.command.PlaceOrderCommand;
import org.atlas.service.order.contract.dto.OrderDto;
import org.atlas.service.order.contract.query.GetOrderStatusQuery;
import org.atlas.service.order.contract.query.ListOrderQuery;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;

  @GetMapping
  public Response<PageDto<OrderDto>> listOrder(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
  ) throws Exception {
    ListOrderQuery query = new ListOrderQuery(page, size);
    PageDto<OrderDto> orderPage = queryGateway.send(query);
    return Response.success(orderPage);
  }

  /**
   * Long polling endpoint that is used to check the order status.
   */
  @GetMapping("/{id}/status")
  public Response<OrderStatus> getOrderStatus(@PathVariable("id") Integer id) throws Exception {
    GetOrderStatusQuery query = new GetOrderStatusQuery(id);
    OrderStatus orderStatus = queryGateway.send(query);
    return Response.success(orderStatus);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Response<Integer> placeOrder(@Valid @RequestBody PlaceOrderCommand command)
      throws Exception {
    Integer newOrderId = commandGateway.send(command);
    return Response.success(newOrderId);
  }
}
