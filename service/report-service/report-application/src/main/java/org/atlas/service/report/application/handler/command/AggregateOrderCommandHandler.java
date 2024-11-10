package org.atlas.service.report.application.handler.command;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.service.report.contract.command.AggregateReportOrderCommand;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AggregateOrderCommandHandler implements CommandHandler<AggregateReportOrderCommand, Void> {

    private final OrderRepository orderRepository;

    @Override
    public Void handle(AggregateReportOrderCommand command) throws Exception {
        return null;
    }
}
