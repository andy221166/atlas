package org.atlas.service.product.adapter.persistence.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaProductEntity;
import org.atlas.service.product.port.outbound.repository.FindProductParams;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class CustomJpaProductRepositoryUsingJpql implements CustomJpaProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<JpaProductEntity> find(FindProductParams params, PagingRequest pagingRequest) {
    StringBuilder sqlBuilder = new StringBuilder("""
        select p
        from JpaProduct p
        left join fetch p.brand
        left join fetch p.detail
        left join fetch p.categories
        """);

    Map<String, Object> queryParams = new HashMap<>();
    sqlBuilder.append(buildWhereClause(params, queryParams));

    // Sorting
    if (pagingRequest.hasSort()) {
      sqlBuilder.append(" order by ").append(pagingRequest.getSortBy());
      if (pagingRequest.isSortDescending()) {
        sqlBuilder.append(" desc");
      }
    }

    String sql = sqlBuilder.toString();
    TypedQuery<JpaProductEntity> query = entityManager.createQuery(sql, JpaProductEntity.class);
    queryParams.forEach(query::setParameter);

    // Paging
    if (pagingRequest.hasPaging()) {
      query.setFirstResult(pagingRequest.getOffset());
      query.setMaxResults(pagingRequest.getLimit());
    }

    return query.getResultList();
  }

  @Override
  public long count(FindProductParams criteria) {
    Map<String, Object> params = new HashMap<>();
    String whereClause = buildWhereClause(criteria, params);
    String countSql = """
        select count(p.id)
        from JpaProductEntity p
        left join fetch p.brand b
        left join fetch p.detail d
        left join fetch p.categories c
        """ + whereClause;
    TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);
    params.forEach(countQuery::setParameter);
    return countQuery.getSingleResult();
  }

  private String buildWhereClause(FindProductParams params, Map<String, Object> queryParams) {
    StringBuilder whereClauseBuilder = new StringBuilder("where 1=1 ");
    if (params.getId() != null) {
      whereClauseBuilder.append(" and p.id = :id ");
      queryParams.put("id", params.getId());
    }
    if (StringUtils.isNotBlank(params.getKeyword())) {
      whereClauseBuilder.append(
          """
              and (
                lower(p.code) like :keyword
                or lower(p.name) like :keyword
                or lower(d.description) like :keyword
              )
              """);
      queryParams.put("keyword", "%" + params.getKeyword().toLowerCase() + "%");
    }
    if (params.getMinPrice() != null) {
      whereClauseBuilder.append(" and p.price >= :minPrice ");
      queryParams.put("minPrice", params.getMinPrice());
    }
    if (params.getMaxPrice() != null) {
      whereClauseBuilder.append(" and p.price <= :maxPrice ");
      queryParams.put("maxPrice", params.getMaxPrice());
    }
    if (params.getStatus() != null) {
      whereClauseBuilder.append(" and p.status = :status ");
      queryParams.put("status", params.getStatus());
    }
    if (params.getAvailableFrom() != null) {
      whereClauseBuilder.append(" and p.availableFrom >= :availableFrom ");
      queryParams.put("availableFrom", params.getAvailableFrom());
    }
    if (params.getIsActive() != null) {
      whereClauseBuilder.append(" and p.isActive = :isActive ");
      queryParams.put("isActive", params.getIsActive());
    }
    if (params.getBrandId() != null) {
      whereClauseBuilder.append(" and b.id = :brandId ");
      queryParams.put("brandId", params.getBrandId());
    }
    if (CollectionUtils.isNotEmpty(params.getCategoryIds())) {
      whereClauseBuilder.append(" and c.id IN (:categoryIds) ");
      queryParams.put("categoryIds", params.getCategoryIds());
    }
    return whereClauseBuilder.toString();
  }
}