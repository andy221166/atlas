package org.atlas.service.product.adapter.api.server.rest.master.controller;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.service.catalog.port.inbound.master.ListCategoryUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/master/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final ListCategoryUseCase listCategoryUseCase;

  @GetMapping
  public Response<ListCategoryUseCase.Output> listCategory() throws Exception {
    ListCategoryUseCase.Output output = listCategoryUseCase.handle(null);
    return Response.success(output);
  }
}
