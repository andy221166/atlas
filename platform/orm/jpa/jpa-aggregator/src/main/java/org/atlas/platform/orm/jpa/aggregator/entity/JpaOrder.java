package org.atlas.platform.orm.jpa.aggregator.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.atlas.service.aggregator.domain.Aggregator;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class JpaOrder {

  @Id
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JpaOrderItem> orderItems;

  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "canceled_reason")
  private String canceledReason;

  @Enumerated(EnumType.STRING)
  private Aggregator aggregator;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JpaOrder other = (JpaOrder) o;
    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public void addOrderItem(JpaOrderItem orderItem) {
    if (this.orderItems == null) {
      this.orderItems = new ArrayList<>();
    }
    this.orderItems.add(orderItem);
    orderItem.setOrder(this);
  }
}
