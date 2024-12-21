package org.atlas.platform.orm.jdbc.user.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.jdbc.user.supports.CustomerRowMapper;
import org.atlas.service.user.domain.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcCustomerRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public Optional<Customer> findByUserId(Integer userId) {
    String sql = """
        select c.* 
        from customer c 
        where c.user_id = :userId
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("userId", userId);
    try {
      Customer customer = namedParameterJdbcTemplate.queryForObject(sql, params,
          new CustomerRowMapper());
      return Optional.ofNullable(customer);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public void insert(Customer customer) {
    String sql = """
        insert into customer (user_id, credit)
        values (:userId, :credit)
        """;
    MapSqlParameterSource params = toParams(customer);
    namedParameterJdbcTemplate.update(sql, params);
  }

  public int decreaseCredit(Integer userId, BigDecimal amount) {
    String sql = """
        update customer c 
        set c.credit = c.credit - :amount 
        where c.user_id = :userId
        and c.credit >= :amount
        """;
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    params.put("amount", amount);
    return namedParameterJdbcTemplate.update(sql, params);
  }

  private MapSqlParameterSource toParams(Customer customer) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("userId", customer.getId());
    params.addValue("credit", customer.getCredit());
    return params;
  }
}
