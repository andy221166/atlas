package org.atlas.service.user.api.client;

import org.atlas.service.user.port.inbound.internal.ListUserUseCase;

public interface UserApiClient {

  ListUserUseCase.Output call(ListUserUseCase.Input input);
}
