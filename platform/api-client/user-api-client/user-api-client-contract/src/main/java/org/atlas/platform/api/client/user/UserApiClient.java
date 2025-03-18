package org.atlas.platform.api.client.user;

import org.atlas.service.user.port.inbound.usecase.internal.ListUserUseCase;

public interface UserApiClient {

  ListUserUseCase.Output call(ListUserUseCase.Input input);
}
