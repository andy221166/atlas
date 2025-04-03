package org.atlas.platform.api.client.user.rest.model;

import java.util.List;
import lombok.Data;

@Data
public class ListUserRequest {

  private List<Integer> ids;
}
