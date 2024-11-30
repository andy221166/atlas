package org.atlas.platform.persistence.jdbc.product.supports;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.atlas.platform.persistence.jdbc.core.NullSafeRowMapper;
import org.atlas.service.product.domain.Category;
import org.atlas.service.product.domain.Product;
import org.springframework.jdbc.core.RowMapper;

public class ProductRowMapper implements RowMapper<Product> {

  @Override
  public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
    NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);

    Product product = new Product();
    product.setId(rowMapper.getInt("id"));
    product.setName(rowMapper.getString("name"));
    product.setDescription(rowMapper.getString("description"));
    product.setPrice(rowMapper.getBigDecimal("price"));
    product.setQuantity(rowMapper.getInt("quantity"));
    product.setCreatedAt(rowMapper.getTimestamp("created_at"));
    product.setUpdatedAt(rowMapper.getTimestamp("updated_at"));

    Category category = new Category();
    category.setId(rowMapper.getInt("category_id"));
    category.setName(rowMapper.getString("category_name"));
    product.setCategory(category);

    return product;
  }
}
