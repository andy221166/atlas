package org.atlas.service.user.contract.query;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.user.contract.model.UserDto;
import org.atlas.service.user.contract.model.UserSimpleDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListUserBulkQuery implements Query<List<UserSimpleDto>> {

  @NotEmpty
  private List<Integer> ids;
}
