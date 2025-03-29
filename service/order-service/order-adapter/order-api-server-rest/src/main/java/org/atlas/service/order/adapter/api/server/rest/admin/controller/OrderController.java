package org.atlas.service.order.adapter.api.server.rest.admin.controller;

import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.adapter.api.server.rest.admin.model.ListOrderResponse;
import org.atlas.service.order.port.inbound.admin.ListOrderUseCase;
import org.atlas.service.order.port.inbound.admin.ListOrderUseCase.ListOrderInput;
import org.atlas.service.order.port.inbound.admin.ListOrderUseCase.ListOrderOutput;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminOrderController")
@RequestMapping("api/admin/orders")
@Validated
public class OrderController {

  private final ListOrderUseCase listOrderUseCase;

  public OrderController(
      @Qualifier("adminListOrderUseCaseHandler") ListOrderUseCase listOrderUseCase) {
    this.listOrderUseCase = listOrderUseCase;
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
    ListOrderResponse response = ObjectMapperUtil.getInstance().map(output, ListOrderResponse.class);
    return Response.success(response);
  }
}
