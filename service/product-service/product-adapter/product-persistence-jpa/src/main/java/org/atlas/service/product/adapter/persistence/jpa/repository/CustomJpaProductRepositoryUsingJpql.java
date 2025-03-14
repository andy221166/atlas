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
import org.atlas.service.product.port.outbound.repository.FindProductCriteria;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class CustomJpaProductRepositoryUsingJpql implements CustomJpaProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<JpaProductEntity> find(FindProductCriteria criteria, PagingRequest pagingRequest) {
    StringBuilder sqlBuilder = new StringBuilder("""
        select p
        from JpaProduct p
        left join fetch p.brand
        left join fetch p.detail
        left join fetch p.categories
        """);

    Map<String, Object> params = new HashMap<>();
    sqlBuilder.append(buildWhereClause(criteria, params));

    // Sorting
    if (pagingRequest.hasSort()) {
      sqlBuilder.append(" order by ").append(pagingRequest.getSortBy());
      if (pagingRequest.isSortDescending()) {
        sqlBuilder.append(" desc");
      }
    }

    String sql = sqlBuilder.toString();
    TypedQuery<JpaProductEntity> query = entityManager.createQuery(sql, JpaProductEntity.class);
    params.forEach(query::setParameter);

    // Paging
    if (pagingRequest.hasPaging()) {
      query.setFirstResult(pagingRequest.getOffset());
      query.setMaxResults(pagingRequest.getLimit());
    }

    return query.getResultList();
  }

  @Override
  public long count(FindProductCriteria criteria) {
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

  private String buildWhereClause(FindProductCriteria criteria, Map<String, Object> params) {
    StringBuilder whereClauseBuilder = new StringBuilder("where 1=1 ");
    if (StringUtils.isNotBlank(criteria.getCode())) {
      whereClauseBuilder.append(" and p.code = :code ");
      params.put("code", criteria.getCode());
    }
    if (StringUtils.isNotBlank(criteria.getKeyword())) {
      whereClauseBuilder.append(
          """
              and (
                lower(p.code) like :keyword
                or lower(p.name) like :keyword
                or lower(d.description) like :keyword
              )
              """);
      params.put("keyword", "%" + criteria.getKeyword().toLowerCase() + "%");
    }
    if (criteria.getMinPrice() != null) {
      whereClauseBuilder.append(" and p.price >= :minPrice ");
      params.put("minPrice", criteria.getMinPrice());
    }
    if (criteria.getMaxPrice() != null) {
      whereClauseBuilder.append(" and p.price <= :maxPrice ");
      params.put("maxPrice", criteria.getMaxPrice());
    }
    if (criteria.getStatus() != null) {
      whereClauseBuilder.append(" and p.status = :status ");
      params.put("status", criteria.getStatus());
    }
    if (criteria.getAvailableFrom() != null) {
      whereClauseBuilder.append(" and p.availableFrom >= :availableFrom ");
      params.put("availableFrom", criteria.getAvailableFrom());
    }
    if (criteria.getIsActive() != null) {
      whereClauseBuilder.append(" and p.isActive = :isActive ");
      params.put("isActive", criteria.getIsActive());
    }
    if (criteria.getBrandId() != null) {
      whereClauseBuilder.append(" and b.id = :brandId ");
      params.put("brandId", criteria.getBrandId());
    }
    if (CollectionUtils.isNotEmpty(criteria.getCategoryIds())) {
      whereClauseBuilder.append(" and c.id IN (:categoryIds) ");
      params.put("categoryIds", criteria.getCategoryIds());
    }
    return whereClauseBuilder.toString();
  }
}