package org.atlas.service.user.adapter.persistence.mybatis;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PageResult;
import org.atlas.platform.commons.paging.PageRequest;
import org.atlas.service.user.adapter.persistence.mybatis.mapper.UserMapper;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.outbound.repository.FindUserCriteria;
import org.atlas.service.user.port.outbound.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final UserMapper userMapper;

  @Override
  public List<UserEntity> findByIdIn(List<Integer> ids) {
    return userMapper.findByIdIn(ids);
  }

  @Override
  public PageResult<UserEntity> findByCriteria(FindUserCriteria criteria, PageRequest pageRequest) {
    long totalCount = userMapper.countByCriteria(criteria);
    if (totalCount == 0) {
      return PageResult.empty();
    }
    List<UserEntity> userEntities = userMapper.findByCriteria(criteria, pageRequest);
    return PageResult.of(userEntities, totalCount);
  }

  @Override
  public Optional<UserEntity> findById(Integer id) {
    return Optional.ofNullable(userMapper.findById(id));
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return Optional.ofNullable(userMapper.findByUsername(username));
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return Optional.ofNullable(userMapper.findByEmail(email));
  }

  @Override
  public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
    return Optional.ofNullable(userMapper.findByPhoneNumber(phoneNumber));
  }

  @Override
  public int insert(UserEntity userEntity) {
    return userMapper.insert(userEntity);
  }
}
