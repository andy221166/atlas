package org.atlas.platform.orm.jpa.task.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.orm.jpa.task.entity.JpaBaseEntity;

@Entity
@Table(name = "order_item")
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaOrderItem extends JpaBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private JpaOrder order;

  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "product_price")
  private BigDecimal productPrice;

  private Integer quantity;
}
