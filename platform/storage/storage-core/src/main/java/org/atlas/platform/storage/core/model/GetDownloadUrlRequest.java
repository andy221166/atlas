package org.atlas.platform.storage.core.model;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetDownloadUrlRequest extends BaseRequest {

  private Duration ttl;
}
