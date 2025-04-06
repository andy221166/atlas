package org.atlas.framework.internalapi;

import org.atlas.domain.user.shared.internal.ListUserInput;
import org.atlas.domain.user.shared.internal.ListUserOutput;

public interface UserApiPort {

  ListUserOutput call(ListUserInput input);
}
