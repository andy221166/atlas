package org.atlas.service.order.contract.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.enums.FileType;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.cqrs.model.Command;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExportOrderCommand extends PagingRequest implements Command<byte[]> {

  @NotNull
  private FileType fileType;
}
