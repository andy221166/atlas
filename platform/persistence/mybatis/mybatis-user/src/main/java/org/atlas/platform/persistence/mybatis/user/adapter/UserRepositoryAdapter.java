package org.atlas.platform.persistence.mybatis.user.adapter;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.persistence.mybatis.user.mapper.CustomerMapper;
import org.atlas.platform.persistence.mybatis.user.mapper.UserMapper;
import org.atlas.service.user.contract.repository.UserRepository;
import org.atlas.service.user.domain.Customer;
import org.atlas.service.user.domain.User;
import org.atlas.service.user.domain.shared.enums.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

  private final UserMapper userMapper;
  private final CustomerMapper customerMapper;

  @Override
  public List<User> findByIdIn(List<Integer> ids) {
    return userMapper.findByIdIn(ids);
  }

  @Override
  public Optional<User> findById(Integer id) {
    return Optional.ofNullable(userMapper.findById(id))
        .map(user -> {
          // If user is found and role is Customer, fetch customer-specific data
          if (user.getRole() == Role.CUSTOMER) {
            Customer customer = ModelMapperUtil.map(user, Customer.class);
            Optional.ofNullable(customerMapper.findByUserId(id))
                .ifPresent(dbCustomer -> ModelMapperUtil.merge(dbCustomer, customer));
            return customer;
          }
          return user;
        });
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(userMapper.findByUsername(username));
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(userMapper.findByEmail(email));
  }

  @Override
  public void insert(User user) {
    userMapper.insert(user);
    customerMapper.insert((Customer) user);
  }
}
