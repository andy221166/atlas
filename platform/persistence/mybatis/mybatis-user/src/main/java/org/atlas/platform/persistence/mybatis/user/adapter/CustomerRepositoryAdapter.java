package org.atlas.platform.persistence.mybatis.user.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.mybatis.user.mapper.UserMapper;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final UserMapper userMapper;

    @Override
    public int decreaseCredit(Integer id, BigDecimal amount) {
        return userMapper.decreaseCredit(id, amount);
    }
}
