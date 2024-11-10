package org.atlas.service.report.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.report.contract.model.ReportDto;
import org.atlas.service.report.contract.query.ReportQuery;
import org.atlas.service.report.contract.repository.ReportRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportQueryHandler implements QueryHandler<ReportQuery, ReportDto> {

    private static final int DEFAULT_LIMIT = 10;

    private final ReportRepository reportRepository;

    @Override
    public ReportDto handle(ReportQuery query) throws Exception {
        ReportDto reportDto = new ReportDto();
        reportDto.setTopHighestAmountOrders(
            reportRepository.findTopHighestAmountOrders(query.getStartDate(), query.getEndDate(), DEFAULT_LIMIT));
        reportDto.setTopBestSoldProducts(
            reportRepository.findTopBestSoldProducts(query.getStartDate(), query.getEndDate(), DEFAULT_LIMIT));
        reportDto.setTopHighestSpentUsers(
            reportRepository.findTopHighestSpentUsers(query.getStartDate(), query.getEndDate(), DEFAULT_LIMIT));
        return null;
    }
}
