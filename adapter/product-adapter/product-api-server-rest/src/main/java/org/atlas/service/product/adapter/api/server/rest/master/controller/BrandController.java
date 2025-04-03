package org.atlas.service.product.adapter.api.server.rest.master.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.adapter.api.server.rest.master.model.ListBrandResponse;
import org.atlas.port.inbound.product.master.ListBrandUseCase;
import org.atlas.port.inbound.product.master.ListBrandUseCase.ListBrandOutput;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/master/brands")
@Validated
@RequiredArgsConstructor
public class BrandController {

  private final ListBrandUseCase listBrandUseCase;

  @Operation(summary = "List Brands", description = "Retrieves a list of all available brands.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListBrandResponse> listBrand() throws Exception {
    ListBrandOutput output = listBrandUseCase.handle(null);
    ListBrandResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListBrandResponse.class);
    return Response.success(response);
  }
}
