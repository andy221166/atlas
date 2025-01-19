package org.atlas.platform.orm.jdbc.user.adapter;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.orm.jdbc.user.repository.JdbcCustomerRepository;
import org.atlas.platform.orm.jdbc.user.repository.JdbcUserRepository;
import org.atlas.service.user.contract.repository.UserRepository;
import org.atlas.service.user.domain.User;
import org.atlas.service.user.domain.shared.enums.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final JdbcUserRepository jdbcUserRepository;
  private final JdbcCustomerRepository jdbcCustomerRepository;

  @Override
  public List<User> findByIdIn(List<Integer> ids) {
    return jdbcUserRepository.findByIdIn(ids);
  }

  @Override
  public Optional<User> findById(Integer id) {
    return jdbcUserRepository.findById(id)
        .map(user -> {
          // If user is found and role is Customer, fetch customer-specific data
          if (user.getRole() == Role.CUSTOMER) {
            Customer customer = ModelMapperUtil.map(user, Customer.class);
            jdbcCustomerRepository.findByUserId(id)
                .ifPresent(dbCustomer -> ModelMapperUtil.merge(dbCustomer, customer));
            return customer;
          }
          return user;
        });
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return jdbcUserRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jdbcUserRepository.findByEmail(email);
  }

  @Override
  public void insert(User user) {
    jdbcUserRepository.insert(user);
    jdbcCustomerRepository.insert((Customer) user);
  }
}
