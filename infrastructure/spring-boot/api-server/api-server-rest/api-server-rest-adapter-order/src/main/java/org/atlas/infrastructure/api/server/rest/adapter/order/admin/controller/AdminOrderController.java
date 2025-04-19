package org.atlas.infrastructure.api.server.rest.adapter.order.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.usecase.admin.AdminListOrderUseCaseHandler;
import org.atlas.domain.order.usecase.admin.AdminListOrderUseCaseHandler.ListOrderInput;
import org.atlas.domain.order.usecase.admin.AdminListOrderUseCaseHandler.ListOrderOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.infrastructure.api.server.rest.adapter.order.admin.model.ListOrderResponse;
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
  public ApiResponseWrapper<ListOrderResponse> listOrder(
      @Parameter(name = "page", description = "The page number to be retrieved (default is 1).", example = "1")
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(name = "size", description = "The number of orders per page (default is defined by the constant).", example = "20")
      @RequestParam(name = "size", required = false, defaultValue = CommonConstant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListOrderInput input = ListOrderInput.builder()
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    ListOrderOutput output = adminListOrderUseCaseHandler.handle(input);
    ListOrderResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListOrderResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
