package org.atlas.service.order.application.handler.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.cqrs.command.CommandHandler;
import org.atlas.service.order.contract.command.ExportOrderCommand;
import org.atlas.service.order.contract.csv.OrderCsvWriter;
import org.atlas.service.order.contract.excel.OrderExcelWriter;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ExportOrderCommandHandler implements CommandHandler<ExportOrderCommand, byte[]> {

  private static final String EXCEL_SHEET_NAME = "Orders";

  private final OrderRepository orderRepository;
  private final OrderCsvWriter orderCsvWriter;
  private final OrderExcelWriter orderExcelWriter;

  @Override
  @Transactional(readOnly = true)
  public byte[] handle(ExportOrderCommand command) throws Exception {
    Integer currentUserId = UserContext.getCurrentUserId();
    PageDto<Order> orderPage = orderRepository.findByUserId(currentUserId, command);
    if (orderPage.isEmpty()) {
      return new byte[0];
    }

    return doExport(command.getFileType(), orderPage.getRecords());
  }

  private byte[] doExport(FileType fileType, List<Order> orders) throws Exception {
    switch (fileType) {
      case CSV -> {
        return orderCsvWriter.write(orders);
      }
      case EXCEL -> {
        return orderExcelWriter.write(orders, EXCEL_SHEET_NAME);
      }
      default -> throw new UnsupportedOperationException("Unsupported file type");
    }
  }
}
