package org.atlas.platform.api.client.user;

import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserOutput;

public interface UserApiClient {

  ListUserOutput call(ListUserInput input);
}
