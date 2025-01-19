package org.atlas.platform.file.excel.poi.product.adapter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.atlas.service.product.contract.file.excel.ProductExcelWriter;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelWriterAdapter implements ProductExcelWriter {

  @Override
  public byte[] write(List<Product> products, String sheetName) throws Exception {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      XSSFSheet sheet = workbook.createSheet(sheetName);
      createHeader(workbook, sheet);
      createRows(workbook, sheet, products);
      sheet.autoSizeColumn(1);
      workbook.write(outputStream);
      return outputStream.toByteArray();
    }
  }

  private void createHeader(XSSFWorkbook workbook, XSSFSheet sheet) {
    Row header = sheet.createRow(0);

    CellStyle headerStyle = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontName("Calibri");
    font.setFontHeightInPoints((short) 12);
    font.setBold(true);
    headerStyle.setFont(font);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("ID");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue("Name");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(2);
    headerCell.setCellValue("Description");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(3);
    headerCell.setCellValue("Price");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(4);
    headerCell.setCellValue("Quantity");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(5);
    headerCell.setCellValue("Category ID");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(6);
    headerCell.setCellValue("Import result");
    headerCell.setCellStyle(headerStyle);
  }

  private void createRows(XSSFWorkbook workbook, XSSFSheet sheet, List<Product> products) {
    int rowIndex = 1;
    for (Product product : products) {
      CellStyle style = workbook.createCellStyle();
      style.setAlignment(HorizontalAlignment.LEFT);
      style.setWrapText(true);

      Row row = sheet.createRow(rowIndex);
      Cell cell = row.createCell(0);
      cell.setCellValue(product.getId());
      cell.setCellStyle(style);

      cell = row.createCell(1);
      cell.setCellValue(product.getName());
      cell.setCellStyle(style);

      cell = row.createCell(2);
      cell.setCellValue(product.getDescription());
      cell.setCellStyle(style);

      cell = row.createCell(3);
      cell.setCellValue(product.getPrice().doubleValue());
      cell.setCellStyle(style);

      cell = row.createCell(4);
      cell.setCellValue(product.getQuantity());
      cell.setCellStyle(style);

      cell = row.createCell(5);
      cell.setCellValue(product.getCategory().getId());
      cell.setCellStyle(style);

      rowIndex++;
    }
  }
}
