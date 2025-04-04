package org.atlas.service.product.adapter.api.server.rest.master.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.adapter.api.server.rest.master.model.ListCategoryResponse;
import org.atlas.port.inbound.product.master.ListCategoryUseCase;
import org.atlas.port.inbound.product.master.ListCategoryUseCase.ListCategoryOutput;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/master/categories")
@Validated
@RequiredArgsConstructor
public class CategoryController {

  private final ListCategoryUseCase listCategoryUseCase;

  @Operation(summary = "List Categories", description = "Retrieves a list of all available categories.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListCategoryResponse> listCategory() throws Exception {
    ListCategoryOutput output = listCategoryUseCase.handle(null);
    ListCategoryResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListCategoryResponse.class);
    return Response.success(response);
  }
}
