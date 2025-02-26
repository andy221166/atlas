package org.atlas.service.product.adapter.api.server.rest.master.controller;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.service.catalog.port.inbound.master.ListBrandUseCase;
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

  @GetMapping
  public Response<ListBrandUseCase.Output> listBrand() throws Exception {
    ListBrandUseCase.Output output = listBrandUseCase.handle(null);
    return Response.success(output);
  }
}
