package org.atlas.platform.storage.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.atlas.commons.function.Callback;

@Data
@AllArgsConstructor
public class DownloadFileRequest {

  private String bucket;
  private String fileName;
  private Callback<byte[]> callback;
}