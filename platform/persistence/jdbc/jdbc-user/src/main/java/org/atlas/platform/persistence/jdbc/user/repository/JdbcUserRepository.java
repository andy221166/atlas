package org.atlas.platform.persistence.jdbc.user.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.user.supports.UserRowMapper;
import org.atlas.service.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<User> findByIdIn(List<Integer> ids) {
    String sql = """
        select u.* 
        from users u 
        where u.id in (:ids)
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("ids", ids);
    return namedParameterJdbcTemplate.query(sql, params, new UserRowMapper());
  }

  public Optional<User> findById(Integer id) {
    String sql = """
        select u.* 
        from users u 
        where u.id = :id
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", id);
    try {
      User user = namedParameterJdbcTemplate.queryForObject(sql, params, new UserRowMapper());
      return Optional.ofNullable(user);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public Optional<User> findByUsername(String username) {
    String sql = """
        select u.* 
        from users u 
        where u.username = :username
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("username", username);
    try {
      User user = namedParameterJdbcTemplate.queryForObject(sql, params, new UserRowMapper());
      return Optional.ofNullable(user);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public Optional<User> findByEmail(String email) {
    String sql = """
        select u.* 
        from users u 
        where u.email = :email
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("email", email);
    try {
      User user = namedParameterJdbcTemplate.queryForObject(sql, params, new UserRowMapper());
      return Optional.ofNullable(user);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  public void insert(User user) {
    String sql = """
        insert into users (username, password, first_name, last_name, email, phone_number)
        values (:username, :password, :firstName, :lastName, :email, :phone_number)
        """;
    MapSqlParameterSource params = toParams(user);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
    Number insertedOrderId = keyHolder.getKey();
    if (insertedOrderId != null) {
      user.setId(insertedOrderId.intValue());
    } else {
      throw new RuntimeException("Failed to retrieve the inserted ID");
    }
  }

  private MapSqlParameterSource toParams(User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("username", user.getUsername());
    params.addValue("password", user.getPassword());
    params.addValue("firstName", user.getFirstName());
    params.addValue("lastName", user.getLastName());
    params.addValue("email", user.getEmail());
    return params;
  }
}
