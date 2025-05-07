package org.atlas.infrastructure.api.server.rest.adapter.order.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.domain.order.usecase.admin.AdminListOrderUseCaseHandler;
import org.atlas.domain.order.usecase.admin.AdminListOrderUseCaseHandler.ListOrderInput;
import org.atlas.domain.order.usecase.admin.AdminListOrderUseCaseHandler.OrderOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.infrastructure.api.server.rest.adapter.order.admin.model.OrderResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/orders")
@Validated
@RequiredArgsConstructor
public class AdminOrderController {

  private final AdminListOrderUseCaseHandler adminListOrderUseCaseHandler;

  @Operation(summary = "List Orders", description = "Retrieves a paginated list of orders.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<List<OrderResponse>> listOrder(
      @Parameter(name = "id", description = "ID", example = "1")
      @RequestParam(name = "id", required = false) Integer id,
      @Parameter(name = "userId", description = "User ID", example = "1")
      @RequestParam(name = "userId", required = false) Integer userId,
      @Parameter(name = "status", description = "Order status")
      @RequestParam(name = "status", required = false) OrderStatus status,
      @Parameter(name = "startDate", description = "Start date")
      @RequestParam(name = "startDate", required = false) Date startDate,
      @Parameter(name = "endDate", description = "End date")
      @RequestParam(name = "endDate", required = false) Date endDate,
      @Parameter(name = "page", description = "The page number to be retrieved (default is 1).", example = "1")
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(name = "size", description = "The number of orders per page (default is defined by the constant).", example = "20")
      @RequestParam(name = "size", required = false, defaultValue = CommonConstant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListOrderInput input = ListOrderInput.builder()
        .id(id)
        .userId(userId)
        .status(status)
        .startDate(startDate)
        .endDate(endDate)
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    PagingResult<OrderOutput> output = adminListOrderUseCaseHandler.handle(input);
    PagingResult<OrderResponse> response = ObjectMapperUtil.getInstance()
        .mapPage(output, OrderResponse.class);
    return ApiResponseWrapper.successPage(response);
  }
}
