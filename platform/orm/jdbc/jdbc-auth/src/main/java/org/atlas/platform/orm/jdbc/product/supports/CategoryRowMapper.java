package org.atlas.platform.orm.jdbc.product.supports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.atlas.platform.orm.jdbc.core.NullSafeRowMapper;
import org.atlas.service.product.domain.Category;
import org.springframework.jdbc.core.RowMapper;

public class CategoryRowMapper implements RowMapper<Category> {

  @Override
  public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
    NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);
    Category category = new Category();
    category.setId(rowMapper.getInt("id"));
    category.setName(rowMapper.getString("name"));
    category.setCreatedAt(rowMapper.getTimestamp("created_at"));
    category.setUpdatedAt(rowMapper.getTimestamp("updated_at"));
    return category;
  }
}
