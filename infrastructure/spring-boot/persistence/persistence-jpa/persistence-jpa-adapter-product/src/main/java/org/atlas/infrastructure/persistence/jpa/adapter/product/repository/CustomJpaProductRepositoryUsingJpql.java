package org.atlas.infrastructure.persistence.jpa.adapter.product.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.atlas.domain.product.repository.FindProductCriteria;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.infrastructure.persistence.jpa.adapter.product.entity.JpaProductEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class CustomJpaProductRepositoryUsingJpql implements CustomJpaProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<JpaProductEntity> findByCriteria(FindProductCriteria criteria, PagingRequest pagingRequest) {
    StringBuilder sqlBuilder = new StringBuilder("""
        select distinct p
        from JpaProductEntity p
        left join p.detail d
        left join p.attributes a
        left join p.brand b
        left join p.categories c
        """);

    Map<String, Object> params = new HashMap<>();
    sqlBuilder.append(buildWhereClause(criteria, params));

    // Sorting
    if (pagingRequest.hasSort()) {
      sqlBuilder.append(" order by p.").append(pagingRequest.getSortBy());
      if (pagingRequest.isSortDescending()) {
        sqlBuilder.append(" desc");
      }
    }

    String sql = sqlBuilder.toString();
    TypedQuery<JpaProductEntity> query = entityManager.createQuery(sql, JpaProductEntity.class);

    // Apply Entity Graph to restrict selected attributes
    EntityGraph<?> entityGraph = entityManager.getEntityGraph("JpaProductEntity.findByCriteria");
    query.setHint("jakarta.persistence.loadgraph", entityGraph);

    params.forEach(query::setParameter);

    // Paging
    if (pagingRequest.hasPaging()) {
      query.setFirstResult(pagingRequest.getOffset());
      query.setMaxResults(pagingRequest.getLimit());
    }

    return query.getResultList();
  }

  @Override
  public long countByCriteria(FindProductCriteria criteria) {
    Map<String, Object> params = new HashMap<>();
    String whereClause = buildWhereClause(criteria, params);
    String countSql = """
        select count(distinct p.id)
        from JpaProductEntity p
        left join p.detail d
        left join p.brand b
        left join p.categories c
        left join JpaProductAttributeEntity a on p.id = a.product.id
        """ + whereClause;
    TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);
    params.forEach(countQuery::setParameter);
    return countQuery.getSingleResult();
  }

  private String buildWhereClause(FindProductCriteria criteria, Map<String, Object> params) {
    StringBuilder whereClauseBuilder = new StringBuilder("where 1=1 ");
    if (criteria.getId() != null) {
      whereClauseBuilder.append(" and p.id = :id ");
      params.put("id", criteria.getId());
    }
    if (StringUtils.isNotBlank(criteria.getKeyword())) {
      whereClauseBuilder.append("""
          and (
            lower(p.name) like :keyword
            or lower(d.description) like :keyword
            or lower(a.value) like :keyword
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
    if (CollectionUtils.isNotEmpty(criteria.getBrandIds())) {
      whereClauseBuilder.append(" and b.id IN (:brandIds) ");
      params.put("brandIds", criteria.getBrandIds());
    }
    if (CollectionUtils.isNotEmpty(criteria.getCategoryIds())) {
      whereClauseBuilder.append(" and c.id IN (:categoryIds) ");
      params.put("categoryIds", criteria.getCategoryIds());
    }
    return whereClauseBuilder.toString();
  }
}
