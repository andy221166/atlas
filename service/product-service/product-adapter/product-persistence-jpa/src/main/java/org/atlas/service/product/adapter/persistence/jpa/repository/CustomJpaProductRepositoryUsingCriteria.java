package org.atlas.service.product.adapter.persistence.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.persistence.jpa.core.specification.QueryFilter;
import org.atlas.platform.persistence.jpa.core.specification.QueryOperator;
import org.atlas.platform.persistence.jpa.core.specification.QuerySpecification;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaProductEntity;
import org.atlas.service.product.port.outbound.repository.FindProductParams;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class CustomJpaProductRepositoryUsingCriteria implements CustomJpaProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<JpaProductEntity> find(FindProductParams params, PagingRequest pagingRequest) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<JpaProductEntity> criteriaQuery = criteriaBuilder.createQuery(JpaProductEntity.class);
    Root<JpaProductEntity> root = criteriaQuery.from(JpaProductEntity.class);

    // Joins
    root.fetch("brand", JoinType.LEFT);
    root.fetch("detail", JoinType.LEFT);
    root.fetch("categories", JoinType.LEFT);

    Specification<JpaProductEntity> spec = buildSpec(params);
    Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
    criteriaQuery.where(predicate);

    // Sorting
    if (pagingRequest.hasSort()) {
      if (pagingRequest.isSortAscending()) {
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(pagingRequest.getSortBy())));
      } else {
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(pagingRequest.getSortBy())));
      }
    }

    TypedQuery<JpaProductEntity> typedQuery = entityManager.createQuery(criteriaQuery);

    // Paging
    if (pagingRequest.hasPaging()) {
      typedQuery.setFirstResult(pagingRequest.getOffset());
      typedQuery.setMaxResults(pagingRequest.getLimit());
    }

    return typedQuery.getResultList();
  }

  @Override
  public long count(FindProductParams params) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
    Root<JpaProductEntity> root = query.from(JpaProductEntity.class);

    query.select(criteriaBuilder.count(root.get("id")));

    Specification<JpaProductEntity> spec = buildSpec(params);
    Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
    query.where(predicate);

    return entityManager.createQuery(query).getSingleResult();
  }

  private Specification<JpaProductEntity> buildSpec(FindProductParams params) {
    QuerySpecification<JpaProductEntity> spec = new QuerySpecification<>();
    if (params.getId() != null) {
      spec.addFilter(
          QueryFilter.of("id", params.getId(), QueryOperator.EQUAL));
    }
    if (StringUtils.isNotBlank(params.getKeyword())) {
      spec.addFilter(QueryFilter.or(
          QueryFilter.Condition.of("code", params.getKeyword(), QueryOperator.LIKE),
          QueryFilter.Condition.of("name", params.getKeyword(), QueryOperator.LIKE),
          QueryFilter.Condition.of("detail.description", params.getKeyword(), QueryOperator.LIKE)
      ));
    }
    if (params.getMinPrice() != null) {
      spec.addFilter(
          QueryFilter.of("price", params.getMinPrice(), QueryOperator.GREATER_THAN_EQUAL));
    }
    if (params.getMaxPrice() != null) {
      spec.addFilter(
          QueryFilter.of("price", params.getMaxPrice(), QueryOperator.LESS_THAN_EQUAL));
    }
    if (params.getStatus() != null) {
      spec.addFilter(
          QueryFilter.of("status", params.getStatus(), QueryOperator.EQUAL));
    }
    if (params.getAvailableFrom() != null) {
      spec.addFilter(
          QueryFilter.of("availableFrom", params.getAvailableFrom(),
              QueryOperator.GREATER_THAN_EQUAL));
    }
    if (params.getIsActive() != null) {
      spec.addFilter(
          QueryFilter.of("isActive", params.getIsActive(), QueryOperator.EQUAL));
    }
    if (params.getBrandId() != null) {
      spec.addFilter(
          QueryFilter.of("brand.id", params.getBrandId(), QueryOperator.EQUAL));
    }
    if (CollectionUtils.isNotEmpty(params.getCategoryIds())) {
      spec.addFilter(
          QueryFilter.of("categories.id", params.getCategoryIds(), QueryOperator.IN));
    }
    return spec;
  }
}
