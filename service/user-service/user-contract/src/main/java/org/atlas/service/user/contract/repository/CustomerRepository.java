package org.atlas.service.user.contract.repository;

import java.math.BigDecimal;

public interface CustomerRepository {

  int decreaseCredit(Integer userId, BigDecimal amount);
}
