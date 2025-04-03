package org.atlas.platform.storage.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DownloadFileRequest extends BaseRequest {

  public DownloadFileRequest(String bucket, String objectKey) {
    super(bucket, objectKey);
  }
}
