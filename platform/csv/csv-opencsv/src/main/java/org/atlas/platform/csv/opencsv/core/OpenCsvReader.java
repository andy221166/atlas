package org.atlas.platform.csv.opencsv.core;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class OpenCsvReader {

  private OpenCsvReader() {
  }

  /**
   * Bean-based style
   */
  public static <T> List<T> read(byte[] fileContent, Class<T> beanClass) throws IOException {
    try (InputStream inputStream = new ByteArrayInputStream(fileContent);
        InputStreamReader reader = new InputStreamReader(inputStream)) {
      CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
          .withType(beanClass)
          .withSkipLines(1) // Skip the first line (header)
          .withIgnoreLeadingWhiteSpace(true)
          .build();
      return csvToBean.parse();
    }
  }
}
