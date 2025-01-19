package org.atlas.service.user.contract.search;

import org.atlas.commons.model.PageDto;
import org.atlas.service.user.contract.query.SearchUserQuery;
import org.atlas.service.user.domain.User;

public interface UserSearchService {

  PageDto<User> search(SearchUserQuery query);
}
