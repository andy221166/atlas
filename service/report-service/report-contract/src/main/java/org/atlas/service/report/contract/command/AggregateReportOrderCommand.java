package org.atlas.service.report.contract.command;

import lombok.Data;
import org.atlas.platform.cqrs.model.Command;

@Data
public class AggregateReportOrderCommand implements Command<Void> {
}
