package org.atlas.service.user.port.outbound.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.platform.commons.paging.PageResult;
import org.atlas.platform.commons.paging.PageRequest;
import org.atlas.service.user.domain.entity.UserEntity;

public interface UserRepository {

  List<UserEntity> findByIdIn(List<Integer> ids);

  PageResult<UserEntity> findByCriteria(FindUserCriteria criteria, PageRequest pageRequest);

  Optional<UserEntity> findById(Integer id);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByPhoneNumber(String phoneNumber);

  int insert(UserEntity userEntity);
}
