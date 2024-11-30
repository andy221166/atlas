package org.atlas.platform.persistence.jpa.product.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.atlas.platform.persistence.jpa.core.specification.QueryFilter;
import org.atlas.platform.persistence.jpa.core.specification.QueryOperator;
import org.atlas.platform.persistence.jpa.core.specification.QuerySpecification;
import org.atlas.platform.persistence.jpa.product.entity.JpaProduct;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CustomJpaProductRepositoryUsingCriteria implements CustomJpaProductRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<JpaProduct> find(SearchProductQuery query) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<JpaProduct> criteriaQuery = criteriaBuilder.createQuery(JpaProduct.class);
    Root<JpaProduct> root = criteriaQuery.from(JpaProduct.class);

    // Use join fetch for the category entity
    root.fetch("category", JoinType.LEFT);

    Specification<JpaProduct> spec = buildSpec(query);
    Predicate predicate = spec.toPredicate(root, criteriaQuery, criteriaBuilder);
    criteriaQuery.where(predicate);

    // Sorting
    if (query.hasSort()) {
      if (query.isSortAscending()) {
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(query.getSort())));
      } else {
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(query.getSort())));
      }
    }

    TypedQuery<JpaProduct> typedQuery = entityManager.createQuery(criteriaQuery);

    // Paging
    if (query.hasPaging()) {
      typedQuery.setFirstResult(query.getOffset());
      typedQuery.setMaxResults(query.getLimit());
    }

    return typedQuery.getResultList();
  }

  @Override
  public long count(SearchProductQuery command) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
    Root<JpaProduct> root = query.from(JpaProduct.class);

    query.select(criteriaBuilder.count(root.get("id")));

    Specification<JpaProduct> spec = buildSpec(command);
    Predicate predicate = spec.toPredicate(root, query, criteriaBuilder);
    query.where(predicate);

    return entityManager.createQuery(query).getSingleResult();
  }

  private Specification<JpaProduct> buildSpec(SearchProductQuery command) {
    QuerySpecification<JpaProduct> spec = new QuerySpecification<>();
    if (StringUtils.hasLength(command.getKeyword())) {
      spec.addFilter(QueryFilter.or(
          QueryFilter.Condition.of("name", command.getKeyword(), QueryOperator.LIKE),
          QueryFilter.Condition.of("description", command.getKeyword(), QueryOperator.LIKE)
      ));
    }
    if (command.getCategoryId() != null) {
      spec.addFilter(QueryFilter.of("category.id", command.getCategoryId(), QueryOperator.EQUAL));
    }
    if (command.getMinPrice() != null) {
      spec.addFilter(
          QueryFilter.of("price", command.getMinPrice(), QueryOperator.GREATER_THAN_EQUAL));
    }
    if (command.getMaxPrice() != null) {
      spec.addFilter(QueryFilter.of("price", command.getMaxPrice(), QueryOperator.LESS_THAN_EQUAL));
    }
    return spec;
  }
}
