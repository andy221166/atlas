package org.atlas.service.product.contract.query;

import java.util.List;
import lombok.Data;
import org.atlas.platform.cqrs.query.Query;
import org.atlas.service.product.contract.model.CategoryDto;

@Data
public class ListCategoryQuery implements Query<List<CategoryDto>> {

}