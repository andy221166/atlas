package org.atlas.platform.persistence.mybatis.user.adapter;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.mybatis.user.mapper.CustomerMapper;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

  private final CustomerMapper customerMapper;

  @Override
  public int decreaseCredit(Integer id, BigDecimal amount) {
    return customerMapper.decreaseCredit(id, amount);
  }
}
