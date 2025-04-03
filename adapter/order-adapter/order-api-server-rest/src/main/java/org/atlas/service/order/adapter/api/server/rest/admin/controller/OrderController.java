package org.atlas.service.order.adapter.api.server.rest.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.adapter.api.server.rest.admin.model.ListOrderResponse;
import org.atlas.port.inbound.order.admin.ListOrderUseCase;
import org.atlas.port.inbound.order.admin.ListOrderUseCase.ListOrderInput;
import org.atlas.port.inbound.order.admin.ListOrderUseCase.ListOrderOutput;
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

  @Operation(summary = "List Orders", description = "Retrieves a paginated list of orders.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListOrderResponse> listOrder(
      @Parameter(name = "page", description = "The page number to be retrieved (default is 1).", example = "1")
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(name = "size", description = "The number of orders per page (default is defined by the constant).", example = "20")
      @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListOrderInput input = ListOrderInput.builder()
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    ListOrderOutput output = listOrderUseCase.handle(input);
    ListOrderResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListOrderResponse.class);
    return Response.success(response);
  }
}
