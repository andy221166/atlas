package org.atlas.domain.user.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;

public interface UserRepository {

  PagingResult<UserEntity> findByCriteria(FindUserCriteria criteria, PagingRequest pagingRequest);

  List<UserEntity> findByIdIn(List<Integer> ids);

  Optional<UserEntity> findById(Integer id);

  void insert(UserEntity userEntity);
}
