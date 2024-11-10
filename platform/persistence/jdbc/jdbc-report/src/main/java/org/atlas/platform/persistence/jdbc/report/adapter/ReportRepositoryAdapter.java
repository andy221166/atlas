package org.atlas.platform.persistence.jdbc.report.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.report.repository.JdbcReportRepository;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.contract.repository.ReportRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportRepositoryAdapter implements ReportRepository {

    private final JdbcReportRepository jdbcReportRepository;

    @Override
    public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
        return jdbcReportRepository.findTopHighestAmountOrders(startDate, endDate, limit);
    }

    @Override
    public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
        return jdbcReportRepository.findTopBestSoldProducts(startDate, endDate, limit);
    }

    @Override
    public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
        return jdbcReportRepository.findTopHighestSpentUsers(startDate, endDate, limit);
    }
}
