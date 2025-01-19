package org.atlas.platform.rest.server.aggregator.controller;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.rest.server.core.response.Response;
import org.atlas.service.aggregator.contract.query.ReportQuery;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

  private final QueryGateway queryGateway;

  @GetMapping
  public Response<ReportDto> report(
      @RequestParam(name = "start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      @RequestParam(name = "end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
  ) throws Exception {
    ReportQuery query = new ReportQuery(startDate, endDate);
    return Response.success(queryGateway.send(query));
  }
}
