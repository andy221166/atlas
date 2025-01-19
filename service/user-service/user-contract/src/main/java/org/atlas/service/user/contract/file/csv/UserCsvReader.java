package org.atlas.service.user.contract.file.csv;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import org.atlas.service.user.contract.file.model.ImportUserItem;

public interface UserCsvReader {

  void read(byte[] fileContent, int chunkSize, Consumer<List<ImportUserItem>> chunkConsumer)
      throws IOException;
}
