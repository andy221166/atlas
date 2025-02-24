package org.atlas.platform.commons.util;

import java.io.File;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

  public static File readResourceFile(String filePath) throws IOException {
    ClassPathResource classPathResource = new ClassPathResource(filePath);
    return classPathResource.getFile();
  }
}
