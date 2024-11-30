package org.atlas.platform.rest.server.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.enums.FileType;
import org.atlas.commons.util.base.DateUtil;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.cqrs.gateway.CommandGateway;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.rest.server.core.response.RestResponse;
import org.atlas.service.order.contract.command.ExportOrderCommand;
import org.atlas.service.order.contract.command.PlaceOrderCommand;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.query.GetOrderQuery;
import org.atlas.service.order.contract.query.ListOrderQuery;
import org.atlas.service.order.domain.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;

  @GetMapping
  public RestResponse<PageDto<OrderDto>> listOrder(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
  ) throws Exception {
    ListOrderQuery query = new ListOrderQuery();
    query.setPage(page - 1);
    query.setSize(size);
    return RestResponse.success(queryGateway.send(query));
  }

  @GetMapping("/{id}")
  public RestResponse<Order> getOrderStatus(@PathVariable("id") Integer id) throws Exception {
    GetOrderQuery query = new GetOrderQuery(id);
    return RestResponse.success(queryGateway.send(query));
  }

  @GetMapping("/export")
  public ResponseEntity<byte[]> export(@RequestParam("fileType") FileType fileType)
      throws Exception {
    ExportOrderCommand command = new ExportOrderCommand();
    command.setFileType(fileType);
    byte[] bytes = commandGateway.send(command);
    HttpHeaders headers = new HttpHeaders();
    String fileName = "export-order-" + DateUtil.now("yyyyMMddHHmmss") + "." + command.getFileType()
        .getExtension();
    headers.add("Content-Disposition", "attachment; filename=" + fileName);
    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(bytes);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestResponse<Integer> placeOrder(@Valid @RequestBody PlaceOrderCommand command)
      throws Exception {
    return RestResponse.success(commandGateway.send(command));
  }
}
