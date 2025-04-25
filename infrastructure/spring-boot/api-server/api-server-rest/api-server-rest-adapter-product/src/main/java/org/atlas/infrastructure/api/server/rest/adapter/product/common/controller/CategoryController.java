package org.atlas.infrastructure.api.server.rest.adapter.product.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.usecase.common.ListCategoryUseCaseHandler;
import org.atlas.domain.product.usecase.common.ListCategoryUseCaseHandler.ListCategoryOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.product.common.model.ListCategoryResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/common/products/categories")
@Validated
@RequiredArgsConstructor
public class CategoryController {

  private final ListCategoryUseCaseHandler listCategoryUseCaseHandler;

  @Operation(summary = "List Categories", description = "Retrieves a list of all available categories.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<ListCategoryResponse> listCategory() throws Exception {
    ListCategoryOutput output = listCategoryUseCaseHandler.handle(null);
    ListCategoryResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListCategoryResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
