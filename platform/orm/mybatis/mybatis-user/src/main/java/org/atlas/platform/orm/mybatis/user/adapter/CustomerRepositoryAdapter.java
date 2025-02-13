package org.atlas.platform.orm.mybatis.user.adapter;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.mybatis.user.mapper.CustomerMapper;
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
