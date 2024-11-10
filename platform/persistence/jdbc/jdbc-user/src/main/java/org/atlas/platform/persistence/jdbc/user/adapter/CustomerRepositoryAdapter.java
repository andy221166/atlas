package org.atlas.platform.persistence.jdbc.user.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.user.repository.JdbcCustomerRepository;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.atlas.service.user.domain.Customer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final JdbcCustomerRepository jdbcCustomerRepository;

    @Override
    public int decreaseCredit(Integer userId, BigDecimal amount) {
        return jdbcCustomerRepository.decreaseCredit(userId, amount);
    }
}
