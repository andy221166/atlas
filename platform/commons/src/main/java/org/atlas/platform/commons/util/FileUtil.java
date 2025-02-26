package org.atlas.platform.commons.util;

import java.io.File;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

@UtilityClass
public class FileUtil {

  public static File readResourceFile(String filePath) throws IOException {
    ClassPathResource classPathResource = new ClassPathResource(filePath);
    return classPathResource.getFile();
  }
}
