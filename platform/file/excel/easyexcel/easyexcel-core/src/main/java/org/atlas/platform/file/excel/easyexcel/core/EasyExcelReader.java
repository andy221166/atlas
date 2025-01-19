package org.atlas.platform.file.excel.easyexcel.core;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EasyExcelReader {

  public static <T> void read(byte[] fileContent, String sheetName, Class<T> beanClass,
      int chunkSize, Consumer<List<T>> chunkConsumer) throws IOException {
    try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
      EasyExcel.read(inputStream, beanClass, new PageReadListener<>(chunkConsumer, chunkSize))
          .sheet(sheetName)
          .doRead();
    }
  }
}
