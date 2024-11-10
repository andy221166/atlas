package org.atlas.platform.persistence.mybatis.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.user.domain.Customer;

import java.math.BigDecimal;

@Mapper
public interface CustomerMapper {

    Customer findByUserId(@Param("userId") Integer userId);
    int insert(@Param("customer") Customer customer);
    int decreaseCredit(@Param("userId") Integer userId, @Param("amount") BigDecimal amount);
}
