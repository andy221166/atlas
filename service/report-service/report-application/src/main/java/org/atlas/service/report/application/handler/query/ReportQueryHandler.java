package org.atlas.service.report.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.report.contract.model.ReportDto;
import org.atlas.service.report.contract.query.ReportQuery;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReportQueryHandler implements QueryHandler<ReportQuery, ReportDto> {

  private static final int DEFAULT_LIMIT = 10;

  private final OrderRepository orderRepository;

  @Override
  @Transactional(readOnly = true)
  public ReportDto handle(ReportQuery query) throws Exception {
    ReportDto reportDto = new ReportDto();
    reportDto.setTotalAmount(
        orderRepository.findTotalAmount(query.getStartDate(), query.getEndDate()));
    reportDto.setTopHighestAmountOrders(
        orderRepository.findTopHighestAmountOrders(query.getStartDate(), query.getEndDate(),
            DEFAULT_LIMIT));
    reportDto.setTopBestSoldProducts(
        orderRepository.findTopBestSoldProducts(query.getStartDate(), query.getEndDate(),
            DEFAULT_LIMIT));
    reportDto.setTopHighestSpentUsers(
        orderRepository.findTopHighestSpentUsers(query.getStartDate(), query.getEndDate(),
            DEFAULT_LIMIT));
    return reportDto;
  }
}
