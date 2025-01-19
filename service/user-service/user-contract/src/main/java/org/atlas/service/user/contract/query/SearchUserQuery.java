package org.atlas.service.user.contract.query;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.enums.Role;
import org.atlas.commons.model.PageDto;
import org.atlas.commons.model.PagingQuery;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.user.contract.model.UserSimpleDto;

@Data
@EqualsAndHashCode(callSuper=false)
public class SearchUserQuery extends PagingQuery implements Query<PageDto<UserSimpleDto>> {

  private Integer id;
  // Search by username, firstName, lastName, email, phoneNumber
  private String keyword;
  private Date birthday;
  private Boolean gender;
  private BigDecimal minBalance;
  private BigDecimal maxBalance;
  private Role role;
  private Integer organizationId;
  private Integer groupId;
}
