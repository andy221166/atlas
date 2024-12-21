package org.atlas.platform.storage.core.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.atlas.commons.function.Callback;

@Data
@AllArgsConstructor
public class UploadFileRequest {

  private String bucket;
  private String fileName;
  private byte[] fileContent;
  private Map<String, String> metadata;
  private Callback<Void> callback;
}
