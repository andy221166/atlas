package org.atlas.platform.storage.contract;

import java.util.Map;
import org.atlas.commons.util.function.Callback;

public interface StorageService {

  void upload(String fileName, byte[] fileContent, Map<String, String> metadata,
      Callback<Void> callback);

  void download(String fileName, Callback<byte[]> callback);

  void delete(String fileName, Callback<Void> callback);
}
