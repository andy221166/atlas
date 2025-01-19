package org.atlas.service.user.contract.file.excel;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import org.atlas.service.user.contract.file.model.ImportUserItem;

public interface UserExcelReader {

  void read(byte[] fileContent, String sheetName, int chunkSize, Consumer<List<ImportUserItem>> chunkConsumer)
      throws IOException;
}
