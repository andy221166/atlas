package org.atlas.infrastructure.api.server.rest.adapter.product.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.usecase.common.ListBrandUseCaseHandler;
import org.atlas.domain.product.usecase.common.ListBrandUseCaseHandler.ListBrandOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.product.common.model.ListBrandResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/common/products/brands")
@Validated
@RequiredArgsConstructor
public class BrandController {

  private final ListBrandUseCaseHandler listBrandUseCaseHandler;

  @Operation(summary = "List Brands", description = "Retrieves a list of all available brands.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<ListBrandResponse> listBrand() throws Exception {
    ListBrandOutput output = listBrandUseCaseHandler.handle(null);
    ListBrandResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListBrandResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
