package org.atlas.service.catalog.adapter.persistence.jpa.repository;

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
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.persistence.jpa.core.specification.QueryFilter;
import org.atlas.platform.persistence.jpa.core.specification.QueryOperator;
import org.atlas.platform.persistence.jpa.core.specification.QuerySpecification;
import org.atlas.service.catalog.adapter.persistence.jpa.entity.JpaProduct;
import org.atlas.service.catalog.port.outbound.repository.FindProductCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomJpaProductRepositoryUsingCriteria implements CustomJpaProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<JpaProduct> find(FindProductCriteria criteria, PagingRequest pagingRequest) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<JpaProduct> criteriaQuery = criteriaBuilder.createQuery(JpaProduct.class);
    Root<JpaProduct> root = criteriaQuery.from(JpaProduct.class);

    // Joins
    root.fetch("brand", JoinType.LEFT);
    root.fetch("detail", JoinType.LEFT);
    root.fetch("categories", JoinType.LEFT);

    Specification<JpaProduct> spec = buildSpec(criteria);
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

    TypedQuery<JpaProduct> typedQuery = entityManager.createQuery(criteriaQuery);

    // Paging
    if (pagingRequest.hasPaging()) {
      typedQuery.setFirstResult(pagingRequest.getOffset());
      typedQuery.setMaxResults(pagingRequest.getLimit());
    }

    return typedQuery.getResultList();
  }

  @Override
  public long count(FindProductCriteria criteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
    Root<JpaProduct> root = query.from(JpaProduct.class);

    query.select(criteriaBuilder.count(root.get("id")));

    Specification<JpaProduct> spec = buildSpec(criteria);
    Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
    query.where(predicate);

    return entityManager.createQuery(query).getSingleResult();
  }

  private Specification<JpaProduct> buildSpec(FindProductCriteria criteria) {
    QuerySpecification<JpaProduct> spec = new QuerySpecification<>();
    if (criteria.getId() != null) {
      spec.addFilter(
          QueryFilter.of("id", criteria.getId(), QueryOperator.EQUAL));
    }
    if (StringUtils.hasLength(criteria.getKeyword())) {
      spec.addFilter(QueryFilter.or(
          QueryFilter.Condition.of("code", criteria.getKeyword(), QueryOperator.LIKE),
          QueryFilter.Condition.of("name", criteria.getKeyword(), QueryOperator.LIKE),
          QueryFilter.Condition.of("detail.description", criteria.getKeyword(), QueryOperator.LIKE)
      ));
    }
    if (criteria.getMinPrice() != null) {
      spec.addFilter(
          QueryFilter.of("price", criteria.getMinPrice(), QueryOperator.GREATER_THAN_EQUAL));
    }
    if (criteria.getMaxPrice() != null) {
      spec.addFilter(
          QueryFilter.of("price", criteria.getMaxPrice(), QueryOperator.LESS_THAN_EQUAL));
    }
    if (criteria.getStatus() != null) {
      spec.addFilter(
          QueryFilter.of("status", criteria.getStatus(), QueryOperator.EQUAL));
    }
    if (criteria.getAvailableFrom() != null) {
      spec.addFilter(
          QueryFilter.of("availableFrom", criteria.getAvailableFrom(),
              QueryOperator.GREATER_THAN_EQUAL));
    }
    if (criteria.getIsActive() != null) {
      spec.addFilter(
          QueryFilter.of("isActive", criteria.getIsActive(), QueryOperator.EQUAL));
    }
    if (criteria.getBrandId() != null) {
      spec.addFilter(
          QueryFilter.of("brand.id", criteria.getBrandId(), QueryOperator.EQUAL));
    }
    if (CollectionUtils.isNotEmpty(criteria.getCategoryIds())) {
      spec.addFilter(
          QueryFilter.of("categories.id", criteria.getCategoryIds(), QueryOperator.IN));
    }
    return spec;
  }
}
