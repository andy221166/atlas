package org.atlas.service.order.contract.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.order.contract.model.OrderDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListOrderQuery extends PagingRequest implements Query<PageDto<OrderDto>> {

}
