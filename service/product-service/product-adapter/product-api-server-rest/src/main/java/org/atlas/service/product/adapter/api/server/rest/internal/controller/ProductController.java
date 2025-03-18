package org.atlas.service.product.adapter.api.server.rest.internal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;
import org.atlas.service.product.adapter.api.server.rest.internal.model.ListProductRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("internalProductController")
@RequestMapping("api/internal/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

  private final ListProductUseCase listProductUseCase;

  @PostMapping("/list")
  public Response<ListProductUseCase.Output> listUser(
      @Valid @RequestBody ListProductRequest request)
      throws Exception {
    ListProductUseCase.Input input = ObjectMapperUtil.getInstance()
        .map(request, ListProductUseCase.Input.class);
    ListProductUseCase.Output output = listProductUseCase.handle(input);
    return Response.success(output);
  }
}
