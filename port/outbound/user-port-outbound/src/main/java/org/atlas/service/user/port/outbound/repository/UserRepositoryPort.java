package org.atlas.service.user.port.outbound.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.domain.user.entity.UserEntity;

public interface UserRepositoryPort {

  PagingResult<UserEntity> findAll(PagingRequest pagingRequest);

  List<UserEntity> findByIdIn(List<Integer> ids);

  Optional<UserEntity> findById(Integer id);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByPhoneNumber(String phoneNumber);

  void insert(UserEntity userEntity);
}
