package org.atlas.service.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;
import org.atlas.service.user.domain.shared.enums.Role;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Customer extends User implements Serializable {

    private BigDecimal credit;
}
