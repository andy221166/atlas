package org.atlas.platform.api.client.product.rest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.api.client.rest.model.internal.Response;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListProductResponse extends Response<ListProductUseCase.Output> {

}
