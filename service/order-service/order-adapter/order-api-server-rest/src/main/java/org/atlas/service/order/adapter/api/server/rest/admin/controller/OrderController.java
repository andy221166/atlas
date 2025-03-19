package org.atlas.service.order.adapter.api.server.rest.admin.controller;

import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.order.port.inbound.usecase.admin.ListOrderUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/orders")
@Validated
public class OrderController {

  private final ListOrderUseCase listOrderUseCase;

  public OrderController(
      @Qualifier("adminListOrderUseCaseHandler") ListOrderUseCase listOrderUseCase) {
    this.listOrderUseCase = listOrderUseCase;
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
}
