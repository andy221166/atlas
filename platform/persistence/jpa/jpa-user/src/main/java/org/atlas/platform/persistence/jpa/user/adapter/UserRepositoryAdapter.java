package org.atlas.platform.persistence.jpa.user.adapter;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.persistence.jpa.user.entity.JpaUser;
import org.atlas.platform.persistence.jpa.user.repository.JpaUserRepository;
import org.atlas.service.user.contract.repository.UserRepository;
import org.atlas.service.user.domain.Customer;
import org.atlas.service.user.domain.User;
import org.atlas.service.user.domain.shared.enums.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final JpaUserRepository jpaUserRepository;

  @Override
  public List<User> findByIdIn(List<Integer> ids) {
    return jpaUserRepository.findAllById(ids)
        .stream()
        .map(jpaUser -> ModelMapperUtil.map(jpaUser, User.class))
        .toList();
  }

  @Override
  public Optional<User> findById(Integer id) {
    return jpaUserRepository.findById(id).map(jpaUser -> {
      if (Role.CUSTOMER.equals(jpaUser.getRole())) {
        return ModelMapperUtil.map(jpaUser, Customer.class);
      } else {
        return ModelMapperUtil.map(jpaUser, User.class);
      }
    });
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return jpaUserRepository.findByUsername(username)
        .map(jpaUser -> ModelMapperUtil.map(jpaUser, User.class));
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpaUserRepository.findByEmail(email)
        .map(jpaUser -> ModelMapperUtil.map(jpaUser, User.class));
  }

  @Override
  public void insert(User user) {
    JpaUser jpaUser = ModelMapperUtil.map(user, JpaUser.class);
    jpaUserRepository.save(jpaUser);
    user.setId(jpaUser.getId());
  }
}
