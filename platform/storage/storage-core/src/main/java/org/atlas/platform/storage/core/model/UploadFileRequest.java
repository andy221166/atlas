package org.atlas.platform.storage.core.model;

import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UploadFileRequest extends BaseRequest {

  private byte[] fileContent;
  private Map<String, String> metadata;

  public UploadFileRequest(String bucket, String objectKey, byte[] fileContent) {
    super(bucket, objectKey);
    this.fileContent = fileContent;
  }
}
