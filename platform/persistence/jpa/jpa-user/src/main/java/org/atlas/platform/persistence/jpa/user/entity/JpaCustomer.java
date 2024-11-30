package org.atlas.platform.persistence.jpa.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "user_id") // Because we have a different primary key name in the child table
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaCustomer extends JpaUser implements Serializable {

  private BigDecimal credit;
}
