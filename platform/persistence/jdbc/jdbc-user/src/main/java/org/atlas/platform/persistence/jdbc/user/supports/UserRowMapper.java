package org.atlas.platform.persistence.jdbc.user.supports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.atlas.platform.persistence.jdbc.core.NullSafeRowMapper;
import org.atlas.service.user.domain.User;
import org.atlas.service.user.domain.shared.enums.Role;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);
    User user = new User();
    user.setId(rowMapper.getInt("id"));
    user.setUsername(rowMapper.getString("username"));
    user.setPassword(rowMapper.getString("password"));
    user.setRole(Role.valueOf(rowMapper.getString("role")));
    user.setFirstName(rowMapper.getString("first_name"));
    user.setLastName(rowMapper.getString("last_name"));
    user.setEmail(rowMapper.getString("email"));
    user.setPhoneNumber(rowMapper.getString("phone_number"));
    user.setCreatedAt(rowMapper.getTimestamp("created_at"));
    user.setUpdatedAt(rowMapper.getTimestamp("updated_at"));
    return user;
  }
}
