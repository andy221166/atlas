package org.atlas.platform.persistence.jpa.user.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jpa.user.repository.JpaCustomerRepository;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;

    @Override
    public int decreaseCredit(Integer userId, BigDecimal amount) {
        return jpaCustomerRepository.decreaseCredit(userId, amount);
    }
}
