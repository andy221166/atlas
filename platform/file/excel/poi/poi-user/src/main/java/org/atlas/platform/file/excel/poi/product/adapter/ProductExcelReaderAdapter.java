package org.atlas.platform.file.excel.poi.product.adapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.atlas.platform.file.excel.poi.core.support.PoiUtil;
import org.atlas.service.product.contract.file.excel.ProductExcelReader;
import org.atlas.service.product.domain.ImportProductItem;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelReaderAdapter implements ProductExcelReader {

  @Override
  public void read(byte[] fileContent, String sheetName, int chunkSize,
      Consumer<List<ImportProductItem>> chunkConsumer) throws IOException {
    try (InputStream inputStream = new ByteArrayInputStream(fileContent);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
      XSSFSheet sheet = workbook.getSheet(sheetName);
      int totalRows = sheet.getLastRowNum();
      int currentRow = 1; // Ignore header

      // Reuse the same list for chunks
      List<ImportProductItem> chunk = new ArrayList<>(chunkSize);

      while (currentRow <= totalRows) {
        int endRow = Math.min(currentRow + chunkSize - 1, totalRows);

        for (int rowIndex = currentRow; rowIndex <= endRow; rowIndex++) {
          Row row = sheet.getRow(rowIndex);
          if (PoiUtil.isNotEmptyRow(row)) {
            ImportProductItem item = readRow(row);
            chunk.add(item);
          }
        }

        // Pass the chunk to the consumer
        if (!chunk.isEmpty()) {
          chunkConsumer.accept(chunk);
          chunk.clear(); // Clear the list for reuse
        }

        // Move to the next chunk
        currentRow += chunkSize;
      }
    }
  }

  private ImportProductItem readRow(Row row) {
    ImportProductItem item = new ImportProductItem();
    item.setId((int) row.getCell(0).getNumericCellValue());
    item.setName(row.getCell(1).getStringCellValue());
    item.setDescription(row.getCell(2).getStringCellValue());
    item.setPrice(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
    item.setQuantity((int) row.getCell(4).getNumericCellValue());
    item.setCategoryId((int) row.getCell(5).getNumericCellValue());
    item.setDeleteFlag(row.getCell(6).getBooleanCellValue());
    return item;
  }
}