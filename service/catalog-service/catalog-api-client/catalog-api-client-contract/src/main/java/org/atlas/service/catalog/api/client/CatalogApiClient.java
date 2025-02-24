package org.atlas.service.catalog.api.client;

import org.atlas.service.catalog.port.inbound.internal.ListProductUseCase;

public interface CatalogApiClient {

  ListProductUseCase.Output call(ListProductUseCase.Input input);
}
