package org.atlas.platform.rest.server.product.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.rest.server.core.response.Response;
import org.atlas.service.product.contract.model.CategoryDto;
import org.atlas.service.product.contract.query.ListCategoryQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final QueryGateway queryGateway;

  @GetMapping
  public Response<List<CategoryDto>> listCategory(ListCategoryQuery command) throws Exception {
    return Response.success(queryGateway.send(command));
  }
}
