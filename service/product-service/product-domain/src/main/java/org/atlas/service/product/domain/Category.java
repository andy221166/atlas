package org.atlas.service.product.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.commons.model.Auditable;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Category extends Auditable implements Serializable {

    @EqualsAndHashCode.Include
    private Integer id;
    private String name;

    public Category(Integer id) {
        this.id = id;
    }
}
