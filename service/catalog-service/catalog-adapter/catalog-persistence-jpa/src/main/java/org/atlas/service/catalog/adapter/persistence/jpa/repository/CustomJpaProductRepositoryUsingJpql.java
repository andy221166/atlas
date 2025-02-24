package org.atlas.service.catalog.adapter.persistence.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.atlas.service.catalog.adapter.persistence.jpa.entity.JpaProduct;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@Primary
public class CustomJpaProductRepositoryUsingJpql implements CustomJpaProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<JpaProduct> find(SearchProductQuery command) {
    StringBuilder sqlBuilder = new StringBuilder("""
        select p 
        from JpaProduct p 
        left join fetch p.categoryEntity 
        """);

    Map<String, Object> params = new HashMap<>();
    sqlBuilder.append(buildWhereClause(command, params));

    // Sorting
    if (command.hasSort()) {
      sqlBuilder.append(" order by ").append(command.getSort());
      if (command.isSortDescending()) {
        sqlBuilder.append(" desc");
      }
    }

    String sql = sqlBuilder.toString();
    TypedQuery<JpaProduct> query = entityManager.createQuery(sql, JpaProduct.class);
    params.forEach(query::setParameter);

    // Paging
    if (command.hasPaging()) {
      query.setFirstResult(command.getOffset());
      query.setMaxResults(command.getLimit());
    }

    return query.getResultList();
  }

  @Override
  public long count(SearchProductQuery command) {
    Map<String, Object> params = new HashMap<>();
    String whereClause = buildWhereClause(command, params);
    String countSql = "select count(p.id) from JpaProduct p " + whereClause;
    TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);
    params.forEach(countQuery::setParameter);
    return countQuery.getSingleResult();
  }

  private String buildWhereClause(SearchProductQuery command, Map<String, Object> params) {
    StringBuilder whereClauseBuilder = new StringBuilder("where 1=1 ");
    if (StringUtils.hasLength(command.getKeyword())) {
      whereClauseBuilder.append(" and lower(p.name) like :name ");
      params.put("name", "%" + command.getKeyword().toLowerCase() + "%");
    }
    if (command.getCategoryId() != null) {
      whereClauseBuilder.append(" and p.categoryEntity.id = :categoryId ");
      params.put("categoryId", command.getCategoryId());
    }
    if (command.getMinPrice() != null) {
      whereClauseBuilder.append(" and p.price >= :minPrice ");
      params.put("minPrice", command.getMinPrice());
    }
    if (command.getMaxPrice() != null) {
      whereClauseBuilder.append(" and p.price <= :maxPrice ");
      params.put("maxPrice", command.getMaxPrice());
    }
    return whereClauseBuilder.toString();
  }
}