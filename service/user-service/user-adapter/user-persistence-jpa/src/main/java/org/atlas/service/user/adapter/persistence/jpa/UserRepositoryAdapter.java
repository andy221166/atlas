package org.atlas.service.user.adapter.persistence.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.persistence.jpa.core.paging.PagingConverter;
import org.atlas.service.user.adapter.persistence.jpa.entity.JpaUser;
import org.atlas.service.user.adapter.persistence.jpa.repository.JpaUserRepository;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.outbound.repository.FindUserCriteria;
import org.atlas.service.user.port.outbound.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final JpaUserRepository jpaUserRepository;

  @Override
  public List<UserEntity> findByIdIn(List<Integer> ids) {
    return jpaUserRepository.findAllById(ids)
        .stream()
        .map(jpaUser -> ObjectMapperUtil.getInstance().map(jpaUser, UserEntity.class))
        .toList();
  }

  @Override
  public PagingResult<UserEntity> findByCriteria(FindUserCriteria criteria, PagingRequest pagingRequest) {
    Pageable pageable = PagingConverter.convert(pagingRequest);
    PagingResult<JpaUser> jpaUserPage = PagingConverter.convert(
        jpaUserRepository.findByKeyword(criteria.getKeyword(), pageable));
    return ObjectMapperUtil.getInstance().mapPage(jpaUserPage, UserEntity.class);
  }

  @Override
  public Optional<UserEntity> findById(Integer id) {
    return jpaUserRepository.findById(id)
        .map(jpaUser -> ObjectMapperUtil.getInstance().map(jpaUser, UserEntity.class));
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return jpaUserRepository.findByUsername(username)
        .map(jpaUser -> ObjectMapperUtil.getInstance().map(jpaUser, UserEntity.class));
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return jpaUserRepository.findByEmail(email)
        .map(jpaUser -> ObjectMapperUtil.getInstance().map(jpaUser, UserEntity.class));
  }

  @Override
  public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
    return jpaUserRepository.findByPhoneNumber(phoneNumber)
        .map(jpaUser -> ObjectMapperUtil.getInstance().map(jpaUser, UserEntity.class));
  }

  @Override
  public int insert(UserEntity userEntity) {
    JpaUser jpaUser = ObjectMapperUtil.getInstance().map(userEntity, JpaUser.class);
    jpaUserRepository.save(jpaUser);
    userEntity.setId(jpaUser.getId());
    return 1;
  }
}
