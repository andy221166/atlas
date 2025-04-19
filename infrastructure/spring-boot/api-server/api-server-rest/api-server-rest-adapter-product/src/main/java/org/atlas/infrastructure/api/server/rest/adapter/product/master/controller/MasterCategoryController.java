package org.atlas.infrastructure.api.server.rest.adapter.product.master.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.usecase.master.MasterListCategoryUseCaseHandler;
import org.atlas.domain.product.usecase.master.MasterListCategoryUseCaseHandler.ListCategoryOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.product.master.model.ListCategoryResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/master/categories")
@Validated
@RequiredArgsConstructor
public class MasterCategoryController {

  private final MasterListCategoryUseCaseHandler masterListCategoryUseCaseHandler;

  @Operation(summary = "List Categories", description = "Retrieves a list of all available categories.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<ListCategoryResponse> listCategory() throws Exception {
    ListCategoryOutput output = masterListCategoryUseCaseHandler.handle(null);
    ListCategoryResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListCategoryResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
