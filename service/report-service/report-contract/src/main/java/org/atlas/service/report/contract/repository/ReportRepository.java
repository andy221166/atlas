package org.atlas.service.report.contract.repository;

import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;

import java.util.Date;
import java.util.List;

public interface ReportRepository {

    List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit);
    List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit);
    List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit);
}
