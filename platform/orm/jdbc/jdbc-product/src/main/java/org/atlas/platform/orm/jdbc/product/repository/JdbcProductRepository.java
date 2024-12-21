package org.atlas.platform.orm.jdbc.product.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.jdbc.product.supports.ProductRowMapper;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.domain.Product;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class JdbcProductRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<Product> findByIdIn(List<Integer> ids) {
    String sql = """
        select * 
        from product p 
        where p.id in (:ids)
        """;
    SqlParameterSource params = new MapSqlParameterSource("ids", ids);
    return namedParameterJdbcTemplate.query(sql, params, new ProductRowMapper());
  }

  public List<Product> find(SearchProductQuery query) {
    StringBuilder sqlBuilder = new StringBuilder("""
        select p.*, c.name as category_name
        from product p
        left join category c on p.category_id = c.id
        """);

    MapSqlParameterSource params = new MapSqlParameterSource();
    sqlBuilder.append(buildWhereClause(query, params));

    // Sorting
    if (query.hasSort()) {
      sqlBuilder.append(" order by ").append(query.getSort());
      if (query.isSortDescending()) {
        sqlBuilder.append(" desc");
      }
    }

    // Paging
    if (query.hasPaging()) {
      sqlBuilder.append(" limit :limit offset :offset");
      params.addValue("limit", query.getLimit())
          .addValue("offset", query.getOffset());
    }

    String sql = sqlBuilder.toString();
    return namedParameterJdbcTemplate.query(sql, params, new ProductRowMapper());
  }

  public long count(SearchProductQuery command) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    String sql = "select count(p.id) from product p " + buildWhereClause(command, params);
    return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
  }

  public int insertBatch(List<Product> products) {
    String sql = """
        insert into product (name, description, price, quantity, category_id)
        values (:name, :description, :price, :quantity, :category_id)
        """;

    SqlParameterSource[] batch = products.stream()
        .map(product -> {
          MapSqlParameterSource params = new MapSqlParameterSource();
          params.addValue("name", product.getName());
          params.addValue("description", product.getDescription());
          params.addValue("price", product.getPrice());
          params.addValue("quantity", product.getQuantity());
          params.addValue("category_id", product.getCategory().getId());
          return params;
        })
        .toArray(SqlParameterSource[]::new);

    int[] result = namedParameterJdbcTemplate.batchUpdate(sql, batch);
    return Arrays.stream(result).sum();
  }

  public int increaseQuantity(Integer id, Integer amount) {
    String sql = """
        update product p 
        set p.quantity = p.quantity + :amount 
        where p.id = :id
        """;
    Map<String, Object> params = new HashMap<>();
    params.put("id", id);
    params.put("amount", amount);
    return namedParameterJdbcTemplate.update(sql, params);
  }

  public int decreaseQuantity(Integer id, Integer amount) {
    String sql = """
        update product p 
        set p.quantity = p.quantity - :amount 
        where p.id = :id 
        and p.quantity >= :amount
        """;
    Map<String, Object> params = new HashMap<>();
    params.put("id", id);
    params.put("amount", amount);
    return namedParameterJdbcTemplate.update(sql, params);
  }

  private String buildWhereClause(SearchProductQuery query, MapSqlParameterSource params) {
    StringBuilder whereClauseBuilder = new StringBuilder("where 1=1 ");
    if (StringUtils.hasLength(query.getKeyword())) {
      whereClauseBuilder.append(
          " and (lower(p.name) like :keyword or lower(description) like :keyword) ");
      params.addValue("keyword", "%" + query.getKeyword().toLowerCase() + "%");
    }
    if (query.getCategoryId() != null) {
      whereClauseBuilder.append(" and category_id = :categoryId ");
      params.addValue("categoryId", query.getCategoryId());
    }
    if (query.getMinPrice() != null) {
      whereClauseBuilder.append(" and price >= :minPrice ");
      params.addValue("minPrice", query.getMinPrice());
    }
    if (query.getMaxPrice() != null) {
      whereClauseBuilder.append(" and price <= :maxPrice ");
      params.addValue("maxPrice", query.getMaxPrice());
    }
    return whereClauseBuilder.toString();
  }
}
