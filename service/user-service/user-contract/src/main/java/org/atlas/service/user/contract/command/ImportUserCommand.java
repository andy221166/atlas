package org.atlas.service.user.contract.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.commons.enums.FileType;
import org.atlas.platform.cqrs.model.Command;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportUserCommand implements Command<Integer> {

  @NotNull
  private FileType fileType;

  @NotEmpty
  private byte[] fileContent;
}
