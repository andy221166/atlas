package org.atlas.service.user.contract.query;

import lombok.Data;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.user.contract.model.ProfileDto;

@Data
public class GetProfileQuery implements Query<ProfileDto> {

}
