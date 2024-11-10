package org.atlas.platform.rest.server.report.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.rest.server.core.response.RestResponse;
import org.atlas.service.report.contract.model.ReportDto;
import org.atlas.service.report.contract.query.ReportQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final QueryGateway queryGateway;

    @GetMapping
    public RestResponse<ReportDto> report(
        @RequestParam(name = "start_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam(name = "end_date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date endDate
    ) throws Exception {
        ReportQuery query = new ReportQuery(startDate, endDate);
        return RestResponse.success(queryGateway.send(query));
    }
}
