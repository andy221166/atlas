package org.atlas.service.product.contract.query;

import lombok.Data;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.product.contract.model.CategoryDto;

import java.util.List;

@Data
public class ListCategoryQuery implements Query<List<CategoryDto>> {
}