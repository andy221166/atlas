package org.atlas.service.user.port.outbound.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.user.domain.entity.UserEntity;

public interface UserRepository {

  List<UserEntity> findByIdIn(List<Integer> ids);

  PagingResult<UserEntity> findByCriteria(FindUserCriteria criteria, PagingRequest pagingRequest);

  Optional<UserEntity> findById(Integer id);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByPhoneNumber(String phoneNumber);

  int insert(UserEntity userEntity);
}
