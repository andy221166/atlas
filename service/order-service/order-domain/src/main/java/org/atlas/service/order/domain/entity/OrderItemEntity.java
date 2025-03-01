package org.atlas.service.order.domain.entity;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.atlas.platform.commons.model.DomainEntity;

@Getter
@Setter
public class OrderItemEntity extends DomainEntity {

  @EqualsAndHashCode.Include
  private Integer id;
  private Integer orderId;
  private Integer productId;
  private BigDecimal productPrice;
  private Integer quantity;
}
