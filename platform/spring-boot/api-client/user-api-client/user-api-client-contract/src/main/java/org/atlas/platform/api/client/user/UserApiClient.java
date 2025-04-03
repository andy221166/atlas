package org.atlas.platform.api.client.user;

import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserInput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserOutput;

public interface UserApiClient {

  ListUserOutput call(ListUserInput input);
}
