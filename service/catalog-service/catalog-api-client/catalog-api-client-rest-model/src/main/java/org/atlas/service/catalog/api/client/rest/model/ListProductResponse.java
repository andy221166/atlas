package org.atlas.service.catalog.api.client.rest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.api.client.rest.model.internal.Response;
import org.atlas.service.catalog.port.inbound.internal.ListProductUseCase;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListProductResponse extends Response<ListProductUseCase.Output> {

}
