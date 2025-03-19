package org.atlas.service.product.adapter.excel.poi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.util.DateUtil;
import org.atlas.platform.file.excel.poi.PoiUtil;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.outbound.file.excel.ProductExcelReaderPort;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelReaderAdapter implements ProductExcelReaderPort {

  private static final int BATCH_SIZE = 100;

  @Override
  public List<ProductRow> read(byte[] fileContent) throws IOException {
    try (InputStream inputStream = new ByteArrayInputStream(fileContent);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
      XSSFSheet sheet = workbook.getSheet(SHEET_NAME);
      List<ProductRow> excelRows = new ArrayList<>();
      int totalRows = sheet.getLastRowNum();
      int currentRow = 1; // Ignore header
      while (currentRow <= totalRows) {
        int endRow = Math.min(currentRow + BATCH_SIZE - 1, totalRows);
        for (int rowIndex = currentRow; rowIndex <= endRow; rowIndex++) {
          Row row = sheet.getRow(rowIndex);
          if (PoiUtil.isNotEmptyRow(row)) {
            ProductRow product = readRow(row);
            excelRows.add(product);
          }
        }
        currentRow += BATCH_SIZE;
      }
      return excelRows;
    }
  }

  private ProductRow readRow(Row row) {
    ProductRow productRow = new ProductRow();
    productRow.setName(row.getCell(1).getStringCellValue());
    productRow.setPrice(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));
    productRow.setQuantity((int) row.getCell(0).getNumericCellValue());
    productRow.setStatus(ProductStatus.valueOf(row.getCell(3).getStringCellValue()));
    productRow.setAvailableFrom(DateUtil.parse(row.getCell(4).getStringCellValue(),
        Constant.DATE_TIME_FORMAT));
    productRow.setIsActive(row.getCell(5).getBooleanCellValue());
    productRow.setBrandId((int) row.getCell(6).getNumericCellValue());
    productRow.setDescription(row.getCell(7).getStringCellValue());
    productRow.setImageUrl(row.getCell(8).getStringCellValue());
    productRow.setCategoryIds(Arrays.stream(row.getCell(9).getStringCellValue().split(","))
        .map(Integer::parseInt)
        .toList());
    return productRow;
  }
}
