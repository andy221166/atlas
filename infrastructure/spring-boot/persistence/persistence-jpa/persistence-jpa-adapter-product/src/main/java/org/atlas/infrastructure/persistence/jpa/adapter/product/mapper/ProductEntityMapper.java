package org.atlas.infrastructure.persistence.jpa.adapter.product.mapper;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.domain.product.entity.BrandEntity;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.entity.ProductAttributeEntity;
import org.atlas.domain.product.entity.ProductDetailsEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.product.entity.JpaBrandEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.product.entity.JpaCategoryEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.product.entity.JpaProductAttributeEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.product.entity.JpaProductDetailsEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.product.entity.JpaProductEntity;

@UtilityClass
public class ProductEntityMapper {

  public static JpaProductEntity map(ProductEntity productEntity) {
    // Product
    JpaProductEntity jpaProductEntity = new JpaProductEntity();
    jpaProductEntity.setId(productEntity.getId());
    jpaProductEntity.setName(productEntity.getName());
    jpaProductEntity.setPrice(productEntity.getPrice());
    jpaProductEntity.setQuantity(productEntity.getQuantity());
    jpaProductEntity.setStatus(productEntity.getStatus());
    jpaProductEntity.setAvailableFrom(productEntity.getAvailableFrom());
    jpaProductEntity.setIsActive(productEntity.getIsActive());

    // Set brand reference by ID only
    if (productEntity.getBrand() != null && productEntity.getBrand().getId() != null) {
      JpaBrandEntity jpaBrandEntity = new JpaBrandEntity();
      jpaBrandEntity.setId(productEntity.getBrand().getId());
      jpaProductEntity.setBrand(jpaBrandEntity);
    }

    // Set details (with back-reference)
    if (productEntity.getDetails() != null) {
      JpaProductDetailsEntity jpaProductDetailsEntity = new JpaProductDetailsEntity();
      jpaProductDetailsEntity.setProduct(jpaProductEntity);
      jpaProductDetailsEntity.setDescription(productEntity.getDetails().getDescription());
      jpaProductEntity.setDetails(jpaProductDetailsEntity);
    }

    // Set attributes (with back-reference)
    if (CollectionUtils.isNotEmpty(productEntity.getAttributes())) {
      for (ProductAttributeEntity productAttributeEntity : productEntity.getAttributes()) {
        JpaProductAttributeEntity jpaProductAttributeEntity = new JpaProductAttributeEntity();
        jpaProductAttributeEntity.setId(productAttributeEntity.getId());
        jpaProductAttributeEntity.setName(productAttributeEntity.getName());
        jpaProductAttributeEntity.setValue(productAttributeEntity.getValue());
        jpaProductEntity.addAttribute(jpaProductAttributeEntity);
      }
    }

    // Set category references by ID only
    if (CollectionUtils.isNotEmpty(productEntity.getCategories())) {
      for (CategoryEntity categoryEntity : productEntity.getCategories()) {
        if (categoryEntity.getId() != null) {
          JpaCategoryEntity jpaCategoryEntity = new JpaCategoryEntity();
          jpaCategoryEntity.setId(categoryEntity.getId());
          jpaProductEntity.addCategory(jpaCategoryEntity);
        }
      }
    }

    return jpaProductEntity;
  }

  public static ProductEntity map(JpaProductEntity jpaProductEntity) {
    // Product
    ProductEntity productEntity = new ProductEntity();
    productEntity.setId(jpaProductEntity.getId());
    productEntity.setName(jpaProductEntity.getName());
    productEntity.setPrice(jpaProductEntity.getPrice());
    productEntity.setQuantity(jpaProductEntity.getQuantity());
    productEntity.setStatus(jpaProductEntity.getStatus());
    productEntity.setAvailableFrom(jpaProductEntity.getAvailableFrom());
    productEntity.setIsActive(jpaProductEntity.getIsActive());
    return productEntity;
  }

  public static ProductEntity mapFull(JpaProductEntity jpaProductEntity) {
    // Product
    ProductEntity productEntity = map(jpaProductEntity);

    // Brand
    if (jpaProductEntity.getBrand() != null) {
      BrandEntity brandEntity = new BrandEntity();
      brandEntity.setId(jpaProductEntity.getBrand().getId());
      brandEntity.setName(jpaProductEntity.getBrand().getName());
      productEntity.setBrand(brandEntity);
    }

    // Details
    if (jpaProductEntity.getDetails() != null) {
      ProductDetailsEntity productDetailsEntity = new ProductDetailsEntity();
      productDetailsEntity.setDescription(jpaProductEntity.getDetails().getDescription());
      productEntity.setDetails(productDetailsEntity);
    }

    // Attributes
    if (CollectionUtils.isNotEmpty(jpaProductEntity.getAttributes())) {
      for (JpaProductAttributeEntity jpaProductAttributeEntity : jpaProductEntity.getAttributes()) {
        ProductAttributeEntity productAttributeEntity = new ProductAttributeEntity();
        productAttributeEntity.setId(jpaProductAttributeEntity.getId());
        productAttributeEntity.setName(jpaProductAttributeEntity.getName());
        productAttributeEntity.setValue(jpaProductAttributeEntity.getValue());
        productEntity.addAttribute(productAttributeEntity);
      }
    }

    // Categories
    if (CollectionUtils.isNotEmpty(jpaProductEntity.getCategories())) {
      for (JpaCategoryEntity jpaCategoryEntity : jpaProductEntity.getCategories()) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(jpaCategoryEntity.getId());
        categoryEntity.setName(jpaCategoryEntity.getName());
        productEntity.addCategory(categoryEntity);
      }
    }

    return productEntity;
  }
}
