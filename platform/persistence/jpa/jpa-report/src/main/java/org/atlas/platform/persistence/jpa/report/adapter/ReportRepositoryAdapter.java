package org.atlas.platform.persistence.jpa.report.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jpa.report.repository.JpaReportRepository;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.contract.repository.ReportRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportRepositoryAdapter implements ReportRepository {

    private final JpaReportRepository jpaReportRepository;

    @Override
    public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
        return jpaReportRepository.findTopHighestAmountOrders(
            startDate, endDate, PageRequest.of(0, limit));
    }

    @Override
    public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
        return jpaReportRepository.findTopBestSoldProducts(
            startDate, endDate, PageRequest.of(0, limit));
    }

    @Override
    public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
        return jpaReportRepository.findTopHighestSpentUsers(
            startDate, endDate, PageRequest.of(0, limit));
    }
}
