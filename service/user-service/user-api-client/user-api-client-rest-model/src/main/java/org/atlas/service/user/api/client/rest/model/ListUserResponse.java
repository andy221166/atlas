package org.atlas.service.user.api.client.rest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.api.client.rest.model.internal.Response;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListUserResponse extends Response<ListUserUseCase.Output> {

}
