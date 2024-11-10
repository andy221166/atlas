package org.atlas.platform.persistence.mybatis.report.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.mybatis.report.mapper.ReportMapper;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.contract.repository.ReportRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryAdapter implements ReportRepository {

    private final ReportMapper reportMapper;

    @Override
    public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
        return reportMapper.findTopHighestAmountOrders(startDate, endDate, limit);
    }

    @Override
    public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
        return reportMapper.findTopBestSoldProducts(startDate, endDate, limit);
    }

    @Override
    public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
        return reportMapper.findTopHighestSpentUsers(startDate, endDate, limit);
    }
}
