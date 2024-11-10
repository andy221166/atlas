package org.atlas.platform.job.scheduler.quartz.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.cqrs.gateway.CommandGateway;
import org.atlas.service.report.contract.command.AggregateReportOrderCommand;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AggregateReportOrderJob implements Job {

    private final CommandGateway commandGateway;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            commandGateway.send(new AggregateReportOrderCommand());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
