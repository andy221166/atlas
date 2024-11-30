package org.atlas.platform.excel.easyexcel.core;

import com.alibaba.excel.EasyExcel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class EasyExcelWriter {

  private EasyExcelWriter() {
  }

  public static <T> byte[] write(List<T> records, String sheetName, Class<T> beanClass)
      throws IOException {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      EasyExcel.write(outputStream, beanClass)
          .sheet(sheetName)
          .doWrite(records);
      return outputStream.toByteArray();
    }
  }
}
