package org.atlas.domain.product.usecase.admin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.framework.file.enums.FileType;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminExportProductInput extends AdminListProductInput {

  private FileType fileType;
}
