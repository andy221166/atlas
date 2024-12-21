package org.atlas.platform.orm.jdbc.user.adapter;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.jdbc.user.repository.JdbcCustomerRepository;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

  private final JdbcCustomerRepository jdbcCustomerRepository;

  @Override
  public int decreaseCredit(Integer userId, BigDecimal amount) {
    return jdbcCustomerRepository.decreaseCredit(userId, amount);
  }
}
