package org.atlas.platform.orm.jpa.user.adapter;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.jpa.user.repository.JpaCustomerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

  private final JpaCustomerRepository jpaCustomerRepository;

  @Override
  public int decreaseCredit(Integer userId, BigDecimal amount) {
    return jpaCustomerRepository.decreaseCredit(userId, amount);
  }
}
