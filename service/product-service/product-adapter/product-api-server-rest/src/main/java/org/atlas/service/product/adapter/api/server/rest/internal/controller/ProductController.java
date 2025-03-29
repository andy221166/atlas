package org.atlas.service.product.adapter.api.server.rest.internal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.adapter.api.server.rest.internal.model.ListProductRequest;
import org.atlas.service.product.adapter.api.server.rest.internal.model.ListProductResponse;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductInput;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductOutput;
import org.springframework.http.MediaType;
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

  @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListProductResponse> listUser(
      @Valid @RequestBody ListProductRequest request)
      throws Exception {
    ListProductInput input = ObjectMapperUtil.getInstance()
        .map(request, ListProductInput.class);
    ListProductOutput output = listProductUseCase.handle(input);
    ListProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListProductResponse.class);
    return Response.success(response);
  }
}
