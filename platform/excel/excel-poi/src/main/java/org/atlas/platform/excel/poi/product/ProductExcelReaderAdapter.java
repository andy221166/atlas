package org.atlas.platform.excel.poi.product;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.atlas.platform.excel.poi.core.PoiUtil;
import org.atlas.service.product.contract.excel.ProductExcelReader;
import org.atlas.service.product.domain.Category;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelReaderAdapter implements ProductExcelReader {

  private static final int BATCH_SIZE = 100;

  @Override
  public List<Product> read(byte[] fileContent) throws IOException {
    try (InputStream inputStream = new ByteArrayInputStream(fileContent);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
      XSSFSheet sheet = workbook.getSheetAt(0);
      List<Product> products = new ArrayList<>();
      int totalRows = sheet.getLastRowNum();
      int currentRow = 1; // Ignore header
      while (currentRow <= totalRows) {
        int endRow = Math.min(currentRow + BATCH_SIZE - 1, totalRows);
        for (int rowIndex = currentRow; rowIndex <= endRow; rowIndex++) {
          Row row = sheet.getRow(rowIndex);
          if (PoiUtil.isNotEmptyRow(row)) {
            Product product = readRow(row);
            products.add(product);
          }
        }
        currentRow += BATCH_SIZE;
      }
      return products;
    }
  }

  private Product readRow(Row row) {
    Product product = new Product();
    product.setName(row.getCell(0).getStringCellValue());
    product.setDescription(row.getCell(1).getStringCellValue());
    product.setPrice(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));
    product.setQuantity((int) row.getCell(3).getNumericCellValue());
    product.setCategory(new Category((int) row.getCell(4).getNumericCellValue()));
    return product;
  }
}