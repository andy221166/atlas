package org.atlas.infrastructure.api.server.rest.adapter.product.master.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.usecase.master.MasterListBrandUseCaseHandler;
import org.atlas.domain.product.usecase.master.MasterListBrandUseCaseHandler.ListBrandOutput;
import org.atlas.framework.api.server.rest.response.Response;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.product.master.model.ListBrandResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/master/brands")
@Validated
@RequiredArgsConstructor
public class MasterBrandController {

  private final MasterListBrandUseCaseHandler masterListBrandUseCaseHandler;

  @Operation(summary = "List Brands", description = "Retrieves a list of all available brands.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListBrandResponse> listBrand() throws Exception {
    ListBrandOutput output = masterListBrandUseCaseHandler.handle(null);
    ListBrandResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListBrandResponse.class);
    return Response.success(response);
  }
}
