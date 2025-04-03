package org.atlas.service.product.adapter.persistence.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.platform.persistence.jpa.core.entity.JpaBaseEntity;
import org.atlas.domain.product.entity.ProductStatus;

@Entity
@Table(name = "product")
@NamedEntityGraphs(
    {
        @NamedEntityGraph(
            name = "JpaProductEntity.findByCriteria",
            attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode("name"),
                @NamedAttributeNode("price"),
                @NamedAttributeNode("imageUrl")
            }
        ),
        @NamedEntityGraph(
            name = "JpaProductEntity.findByIdWithAssociations",
            attributeNodes = {
                @NamedAttributeNode("detail"),
                @NamedAttributeNode("attributes"),
                @NamedAttributeNode("brand"),
                @NamedAttributeNode("categories")
            }
        )
    }
)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaProductEntity extends JpaBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Integer id;

  private String name;

  private BigDecimal price;

  @Column(name = "image_url")
  private String imageUrl;

  private Integer quantity;

  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  @Column(name = "available_from")
  private Date availableFrom;

  @Column(name = "is_active")
  private Boolean isActive;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id")
  private JpaBrandEntity brand;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "id", referencedColumnName = "product_id")
  private JpaProductDetailEntity detail;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
  private Set<JpaProductAttributeEntity> attributes = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "product_category",
      joinColumns = {@JoinColumn(name = "product_id")},
      inverseJoinColumns = {@JoinColumn(name = "category_id")}
  )
  private Set<JpaCategoryEntity> categories = new HashSet<>();
}
