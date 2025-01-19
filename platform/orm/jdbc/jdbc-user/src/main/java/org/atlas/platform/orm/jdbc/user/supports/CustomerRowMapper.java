package org.atlas.platform.orm.jdbc.user.supports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.atlas.platform.orm.jdbc.core.NullSafeRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class CustomerRowMapper implements RowMapper<Customer> {

  @Override
  public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
    NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);
    Customer customer = new Customer();
    customer.setId(rowMapper.getInt("user_id"));
    customer.setCredit(rowMapper.getBigDecimal("credit"));
    customer.setCreatedAt(rowMapper.getTimestamp("created_at"));
    customer.setUpdatedAt(rowMapper.getTimestamp("updated_at"));
    return customer;
  }
}
