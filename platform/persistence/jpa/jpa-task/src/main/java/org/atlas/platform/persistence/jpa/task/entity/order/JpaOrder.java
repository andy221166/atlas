package org.atlas.platform.persistence.jpa.task.entity.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.persistence.jpa.task.entity.JpaBaseEntity;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaOrder extends JpaBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JpaOrderItem> orderItems;

  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "canceled_reason")
  private String canceledReason;
}
