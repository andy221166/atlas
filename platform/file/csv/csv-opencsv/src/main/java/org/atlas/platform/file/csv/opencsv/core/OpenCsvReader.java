package org.atlas.platform.file.csv.opencsv.core;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenCsvReader {

  /**
   * Reads rows from a CSV file in chunks and processes each chunk using the provided handler.
   */
  public static <T> void read(byte[] fileContent, Class<T> beanClass, int chunkSize,
      Consumer<List<T>> chunkConsumer) throws IOException {
    List<T> chunk = new ArrayList<>(chunkSize);

    try (InputStream inputStream = new ByteArrayInputStream(fileContent);
        InputStreamReader reader = new InputStreamReader(inputStream);
        CSVReader csvReader = new CSVReader(reader)) {

      // Skip header
      csvReader.readNext();

      ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<>();
      strategy.setType(beanClass);

      CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
          .withMappingStrategy(strategy)
          .build();

      for (T record : csvToBean) {
        chunk.add(record);

        if (chunk.size() == chunkSize) {
          chunkConsumer.accept(new ArrayList<>(chunk));
          chunk.clear();
        }
      }

      // Process remaining records
      if (!chunk.isEmpty()) {
        chunkConsumer.accept(chunk);
      }
    } catch (CsvValidationException e) {
      throw new RuntimeException(e);
    }
  }
}
