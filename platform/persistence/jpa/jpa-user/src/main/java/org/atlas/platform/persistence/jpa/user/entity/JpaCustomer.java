package org.atlas.platform.persistence.jpa.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "customer")
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class JpaCustomer extends JpaUser implements Serializable {

    private BigDecimal credit;
}
